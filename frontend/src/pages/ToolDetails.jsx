import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function ToolDetails() {
  const { id } = useParams(); 
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole');

  // Security Check
  useEffect(() => {
    if (userRole !== 'INVENTORY') {
      navigate('/dashboard');
    }
  }, [userRole, navigate]);

  const [movement, setMovement] = useState({
    toolId: id,
    movementType: 'ISSUE', 
    quantity: 1, 
    machineId: '',
    projectId: '',
    remarks: ''
  });

  // --- SERIALIZED STATE ---
  const [availableSerials, setAvailableSerials] = useState([]);
  const [selectedSerials, setSelectedSerials] = useState([]); 
  const [stockInSerials, setStockInSerials] = useState(['']); // For new stock entry

  const [history, setHistory] = useState([]);
  const [message, setMessage] = useState(null);
  const [machines, setMachines] = useState([]);
  const [showHistory, setShowHistory] = useState(false);
  const [projects, setProjects] = useState([]);

  // Fetch Master Data
 // Fetch Master Data (Machines, Projects, History)
  useEffect(() => {
    // Grab the active department from memory so we only load relevant projects
    const activeDeptId = localStorage.getItem('activeDeptId');

    const fetchMasterData = async () => {
      
      // 1. Fetch Machines Independently
      try {
        const machineRes = await axios.get('http://localhost:8080/api/machines');
        setMachines(machineRes.data);
      } catch (error) {
        console.error("Failed to load machines", error);
      }

      // 2. Fetch Projects Independently (Using the smart department endpoint!)
      try {
        const projectUrl = activeDeptId 
            ? `http://localhost:8080/api/projects/${activeDeptId}` 
            : 'http://localhost:8080/api/projects';
            
        const projectRes = await axios.get(projectUrl);
        setProjects(projectRes.data);
      } catch (error) {
        console.error("Failed to load projects", error);
      }

      // 3. Fetch Movement History Independently
      try {
        const historyRes = await axios.get(`http://localhost:8080/api/movements/tool/${id}`);
        setHistory(historyRes.data);
      } catch (error) {
        console.error("Failed to load movement history", error);
      }
      
    };

    fetchMasterData();
  }, [id]);

  // FETCH DYNAMIC SERIAL NUMBERS
  useEffect(() => {
    let targetStatus = '';
    if (movement.movementType === 'ISSUE') targetStatus = 'AVAILABLE';
    else if (movement.movementType === 'RETURN') targetStatus = 'IN_USE';
    else if (movement.movementType === 'SHARPEN_OUT') targetStatus = 'AVAILABLE';
    else if (movement.movementType === 'SHARPEN_IN') targetStatus = 'SHARPENING';
    else if (movement.movementType === 'SCRAP') targetStatus = 'AVAILABLE'; 

    if (targetStatus && id && movement.movementType !== 'STOCK_IN') {
      axios.get(`http://localhost:8080/api/tool-instances/${id}/status/${targetStatus}`)
        .then(res => {
          setAvailableSerials(res.data);
          setSelectedSerials([]); 
        })
        .catch(err => console.error("Error fetching serials", err));
    } else {
      setAvailableSerials([]);
      setSelectedSerials([]);
    }
  }, [movement.movementType, id]);

  // 🚀 THE FIX: Handles dynamically adding blank input boxes for NEW stock
  const handleStockInQuantityChange = (e) => {
    let newQty = parseInt(e.target.value);
    if (isNaN(newQty) || newQty < 1) newQty = 1;

    const newSerials = [...stockInSerials];
    while (newSerials.length < newQty) newSerials.push('');
    if (newSerials.length > newQty) newSerials.length = newQty;
    setStockInSerials(newSerials);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // 🚀 THE FIX: Decide which array of serials to validate and send
    const activeSerials = movement.movementType === 'STOCK_IN' ? stockInSerials : selectedSerials;

    if (activeSerials.length === 0 || activeSerials.some(s => s.trim() === '')) {
      alert("Please select or fill out all specific tool serial numbers!");
      return;
    }

    try {
      const payload = {
        ...movement,
        machineId: movement.machineId ? parseInt(movement.machineId) : null,
        projectId: movement.projectId ? parseInt(movement.projectId) : null,
        quantity: activeSerials.length,
        serials: activeSerials // 🚀 Send the exact list to Java
      };

      const response = await axios.post('http://localhost:8080/api/movements', payload);
      
      if (response.data.status === true) {
        setMessage({ type: 'success', text: response.data.message });
        setTimeout(() => navigate('/dashboard'), 1000);
      }
    } catch (error) {
      setMessage({ type: 'danger', text: 'Failed to record movement.' });
    }
  };

  return (
    <div className="container mt-5 mb-5">
      <button className="btn btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
        ← Back to Dashboard
      </button>

      <div className="card shadow-sm border-0 mx-auto" style={{ maxWidth: '600px' }}>
        <div className="card-header bg-white py-3">
          <h4 className="mb-0 fw-bold text-primary">Record Inventory Movement</h4>
          <span className="text-muted small">Tool ID: {id}</span>
        </div>
        
        <div className="card-body p-4">
          {message && <div className={`alert alert-${message.type}`}>{message.text}</div>}

          <form onSubmit={handleSubmit}>
            
            <div className="mb-4">
              <label className="form-label fw-bold text-dark">Transaction Type</label>
              <select 
                className="form-select form-select-lg bg-light border-0 fw-semibold text-primary" 
                value={movement.movementType}
                onChange={(e) => setMovement({...movement, movementType: e.target.value})}
              >
                <option value="ISSUE">Issue Tool (Decrease Stock)</option>
                <option value="RETURN">Return Tool (Increase Stock)</option>
                <option value="STOCK_IN">New Stock In (Increase Stock)</option>
                <option value="SHARPEN_OUT">Send to Sharpening (Decrease Stock)</option>
                <option value="SHARPEN_IN">Receive from Sharpening (Increase Stock)</option>
                <option value="SCRAP">Scrap / Dispose (Decrease Stock)</option>
              </select>
            </div>

            {/* --- DYNAMIC UI SWITCH --- */}
            {movement.movementType === 'STOCK_IN' ? (
              <div className="mb-4 p-3 border rounded bg-light">
                <div className="mb-3">
                  <label className="form-label fw-semibold text-primary">Quantity of New Stock</label>
                  <input type="number" className="form-control border-primary" required min="1"
                    value={stockInSerials.length} 
                    onChange={handleStockInQuantityChange} 
                  />
                </div>
                <label className="form-label fw-bold mb-2">Scan or Type New Serial Numbers</label>
                <div className="row g-2">
                  {stockInSerials.map((serial, index) => (
                    <div key={index} className="col-md-6">
                      <div className="input-group input-group-sm">
                        <span className="input-group-text bg-white text-muted fw-bold">#{index + 1}</span>
                        <input 
                          type="text" 
                          className="form-control border-start-0" 
                          placeholder="Enter OEM Serial..."
                          required
                          value={serial}
                          onChange={(e) => {
                            const updated = [...stockInSerials];
                            updated[index] = e.target.value;
                            setStockInSerials(updated);
                          }}
                        />
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ) : (
              <div className="mb-4">
                <label className="form-label fw-bold">Select Specific Tools</label>
                {availableSerials.length === 0 ? (
                  <div className="alert alert-warning py-2 small mb-0 fw-semibold">
                    No tools currently available for this action.
                  </div>
                ) : (
                  <div className="border rounded p-2 bg-white shadow-sm" style={{ maxHeight: '180px', overflowY: 'auto' }}>
                    {availableSerials.map(instance => (
                      <div key={instance.instanceId} className="form-check mb-2">
                        <input 
                          className="form-check-input" 
                          type="checkbox" 
                          value={instance.serialNumber}
                          id={`serial-${instance.instanceId}`}
                          checked={selectedSerials.includes(instance.serialNumber)}
                          onChange={(e) => {
                            if (e.target.checked) {
                              setSelectedSerials([...selectedSerials, instance.serialNumber]);
                            } else {
                              setSelectedSerials(selectedSerials.filter(s => s !== instance.serialNumber));
                            }
                          }}
                        />
                        <label className="form-check-label fw-semibold text-primary" htmlFor={`serial-${instance.instanceId}`} style={{ fontFamily: 'monospace', fontSize: '1.1rem' }}>
                          {instance.serialNumber}
                        </label>
                      </div>
                    ))}
                  </div>
                )}
                <div className="form-text mt-2">
                  Total Selected: <span className="fw-bold text-dark fs-6">{selectedSerials.length}</span>
                </div>
              </div>
            )}

            {movement.movementType === 'ISSUE' && (
              <div className="row mb-3">
                <div className="col">
                  <label className="form-label fw-semibold">Machine (Optional)</label>
                  <select 
                    className="form-select bg-light"
                    value={movement.machineId}
                    onChange={(e) => setMovement({...movement, machineId: e.target.value})}
                  >
                    <option value="">-- Select Machine --</option>
                    {machines.map(m => (
                      <option key={m.machineId} value={m.machineId}>{m.machineName}</option>
                    ))}
                  </select>
                </div>
                
                <div className="col">
                  <label className="form-label fw-semibold">Project (Optional)</label>
                  <select 
                    className="form-select bg-light"
                    value={movement.projectId}
                    onChange={(e) => setMovement({...movement, projectId: e.target.value})}
                  >
                    <option value="">-- Select Project --</option>
                    {projects.map(p => (
                      <option key={p.projectId} value={p.projectId}>{p.projectName}</option>
                    ))}
                  </select>
                </div>
              </div>
            )}

            <div className="mb-4">
              <label className="form-label fw-semibold">Remarks / Notes</label>
              <input type="text" className="form-control bg-light" placeholder="e.g., Broken edge, given to John"
                value={movement.remarks} 
                onChange={(e) => setMovement({...movement, remarks: e.target.value})} 
              />
            </div>

            <button type="submit" className="btn btn-primary w-100 fw-bold py-2 rounded-pill shadow-sm">
              Confirm Transaction
            </button>
          </form>
        </div>
      </div>
      
      {/* Ledger UI Below remains the same */}
      <div className="text-center mt-5 mb-4">
        <button className="btn btn-outline-secondary fw-bold rounded-pill px-4 shadow-sm" onClick={() => setShowHistory(!showHistory)}>
          {showHistory ? 'Hide Movement History ↑' : 'View Movement History ↓'}
        </button>
      </div>

      {showHistory && (
        <div className="card shadow-sm border-0 mx-auto mb-5" style={{ maxWidth: '800px' }}>
          <div className="card-header bg-dark text-white py-3 d-flex justify-content-between align-items-center">
            <h5 className="mb-0 fw-bold">Movement History Ledger</h5>
            <span className="badge bg-secondary">{history.length} Records</span>
          </div>
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead className="table-light">
                <tr>
                  <th>Date</th>
                  <th>Type</th>
                  <th>Qty</th>
                  <th>Serials</th> {/* 🚀 Added Header */}
                  <th>Machine</th>
                  <th>Project</th>
                  <th>Remarks</th>
                </tr>
              </thead>
              <tbody>
                {history.length === 0 ? (
                  <tr><td colSpan="7" className="text-center py-4 text-muted">No movements recorded yet.</td></tr>
                ) : (
                  history.map((record) => (
                    <tr key={record.movementId}>
                      <td className="text-muted small">{new Date(record.movementDate).toLocaleString()}</td>
                      <td>
                        <span className={`badge ${record.movementType === 'ISSUE' || record.movementType === 'SCRAP' || record.movementType === 'SHARPEN_OUT' ? 'bg-danger' : 'bg-success'}`}>
                          {record.movementType}
                        </span>
                      </td>
                      <td className="fw-bold">{record.quantity}</td>
                      
                      {/* 🚀 Show the serials with a monospace font so they look like codes */}
                      <td className="small font-monospace text-primary fw-semibold" style={{ maxWidth: '150px' }}>
                        {record.involvedSerials || '-'}
                      </td>
                      
                      <td>{record.machineId ? `ID: ${record.machineId}` : '-'}</td>
                      <td>{record.projectId ? `ID: ${record.projectId}` : '-'}</td>
                      <td className="small text-secondary">{record.remarks || '-'}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}