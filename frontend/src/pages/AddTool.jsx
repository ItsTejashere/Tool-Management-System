import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function AddTool() {
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole');
  const activeProjectId = localStorage.getItem('activeProjectId');

  useEffect(() => {
    if (userRole !== 'INVENTORY') {
      navigate('/dashboard');
    }
  }, [userRole, navigate]);

  const [formData, setFormData] = useState({
    toolCode: '',
    toolName: '',
    minimumQuantity: '',
    storageLocation: ''
  });

  // 🚀 NEW: State to manage the dynamic list of serial numbers
  const [serials, setSerials] = useState(['']); // Starts with 1 empty input

  const [message, setMessage] = useState(null);

  // Handle changing the "Total Stock" number
  const handleQuantityChange = (e) => {
    let newQty = parseInt(e.target.value);
    if (isNaN(newQty) || newQty < 1) newQty = 1;

    const newSerials = [...serials];
    // Add new blank inputs if quantity increased
    while (newSerials.length < newQty) {
      newSerials.push('');
    }
    // Remove inputs if quantity decreased
    if (newSerials.length > newQty) {
      newSerials.length = newQty;
    }
    setSerials(newSerials);
  };

  // Handle typing into a specific serial number box
  const handleSerialChange = (index, value) => {
    const updatedSerials = [...serials];
    updatedSerials[index] = value;
    setSerials(updatedSerials);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validation: Make sure no serial numbers are blank
    if (serials.some(s => s.trim() === '')) {
      alert("Please fill out all serial number fields, or reduce the Total Stock.");
      return;
    }

    // Validation: Check for duplicate serials in the form
    const uniqueSerials = new Set(serials);
    if (uniqueSerials.size !== serials.length) {
      alert("You cannot enter duplicate serial numbers for the same tool.");
      return;
    }

    try {
      const payload = {
        ...formData,
        projectId: activeProjectId ? parseInt(activeProjectId) : null,
        serials: serials // Send the array of strings to Java!
      };

      const response = await axios.post('http://localhost:8080/api/tools', payload);
      if (response.data.status === true) {
        setMessage({ type: 'success', text: response.data.message });
        setTimeout(() => navigate('/dashboard'), 1000);
      }
    } catch (error) {
      setMessage({ type: 'danger', text: 'Error connecting to server.' });
    }
  };

  return (
    <div className="container mt-5 mb-5">
      <button className="btn btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
        ← Back to Dashboard
      </button>

      <div className="card shadow-sm border-0 mx-auto" style={{ maxWidth: '600px' }}>
        <div className="card-header bg-white py-3">
          <h4 className="mb-0 fw-bold text-primary">Add New Tool</h4>
        </div>
        
        <div className="card-body p-4">
          {message && (
            <div className={`alert alert-${message.type} rounded-3`}>{message.text}</div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label fw-semibold">Tool Code</label>
              <input type="text" className="form-control bg-light" required placeholder="e.g., EM-10-CAR"
                value={formData.toolCode} 
                onChange={(e) => setFormData({...formData, toolCode: e.target.value})} />
            </div>

            <div className="mb-3">
              <label className="form-label fw-semibold">Tool Name</label>
              <input type="text" className="form-control bg-light" required placeholder="e.g., 10mm Carbide End Mill"
                value={formData.toolName} 
                onChange={(e) => setFormData({...formData, toolName: e.target.value})} />
            </div>

            <div className="row mb-3">
              <div className="col">
                <label className="form-label fw-semibold">Min Quantity Alert</label>
                <input type="number" className="form-control bg-light" required min="0"
                  value={formData.minimumQuantity} 
                  onChange={(e) => setFormData({...formData, minimumQuantity: e.target.value})} />
              </div>
              <div className="col">
                <label className="form-label fw-semibold text-primary">Initial Total Stock</label>
                <input type="number" className="form-control border-primary" required min="1"
                  value={serials.length} 
                  onChange={handleQuantityChange} />
              </div>
            </div>

            <div className="mb-4">
              <label className="form-label fw-semibold">Storage Location</label>
              <input type="text" className="form-control bg-light" required placeholder="e.g., Drawer A2"
                value={formData.storageLocation} 
                onChange={(e) => setFormData({...formData, storageLocation: e.target.value})} />
            </div>

            {/* 🚀 DYNAMIC SERIAL NUMBER INPUTS */}
            <div className="mb-4 p-3 border rounded bg-light">
              <label className="form-label fw-bold mb-3">Scan or Type Serial Numbers</label>
              <div className="row g-2">
                {serials.map((serial, index) => (
                  <div key={index} className="col-md-6">
                    <div className="input-group input-group-sm">
                      <span className="input-group-text bg-white text-muted fw-bold">#{index + 1}</span>
                      <input 
                        type="text" 
                        className="form-control border-start-0" 
                        placeholder="Enter OEM Serial..."
                        required
                        value={serial}
                        onChange={(e) => handleSerialChange(index, e.target.value)}
                      />
                    </div>
                  </div>
                ))}
              </div>
            </div>

            <button type="submit" className="btn btn-primary w-100 fw-bold py-2 rounded-pill shadow-sm">
              Save Tool & Serials
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}