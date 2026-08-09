import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import API_URL from '../config';

export default function EditTool() {
  const { id } = useParams(); 
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole');

  useEffect(() => {
    if (userRole !== 'INVENTORY') navigate('/dashboard');
  }, [userRole, navigate]);

  const [formData, setFormData] = useState({
    toolCode: '',
    toolName: '',
    drawingNumber: '',
    specNumber: '',
    minimumQuantity: '',
    storageLocation: '',
    status: ''
  });

  // 🚀 NEW: State to handle dynamic serial numbers during edit/restock
  const [serials, setSerials] = useState([]);
  const [message, setMessage] = useState(null);

  // FETCH existing tool details
  useEffect(() => {
    const fetchToolDetails = async () => {
      try {
        const response = await axios.get(`${API_URL}/api/tools/${id}`);
        setFormData(response.data);
      } catch (error) {
        setMessage({ type: 'danger', text: 'Error loading tool details.' });
      }
    };
    fetchToolDetails();
  }, [id]);

  // Handle dynamic quantity changes for serial generation
  const handleQuantityChange = (e) => {
    let newQty = parseInt(e.target.value);
    if (isNaN(newQty) || newQty < 0) newQty = 0;

    setFormData({...formData, totalQuantity: newQty});

    const newSerials = [...serials];
    while (newSerials.length < newQty) {
      newSerials.push('');
    }
    if (newSerials.length > newQty) {
      newSerials.length = newQty;
    }
    setSerials(newSerials);
  };

  const handleSerialChange = (index, value) => {
    const updatedSerials = [...serials];
    updatedSerials[index] = value;
    setSerials(updatedSerials);
  };

  // SAVE changes
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (serials.some(s => s.trim() === '')) {
      alert("Please fill out all physical serial number fields!");
      return;
    }

    try {
      const payload = {
        ...formData,
        serials: serials // Send the new serials to Java!
      };

      const response = await axios.put(`${API_URL}/api/tools/${id}`, payload);
      if (response.data.status === true) {
        setMessage({ type: 'success', text: response.data.message });
        setTimeout(() => navigate('/dashboard'), 1000);
      }
    } catch (error) {
      setMessage({ type: 'danger', text: 'Error updating tool.' });
    }
  };

  return (
    <div className="container mt-5 mb-5">
      <button className="btn btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
        ← Back to Dashboard
      </button>

      <div className="card panel-card border-0 mx-auto" style={{ maxWidth: '600px' }}>
        <div className="card-header bg-white py-3">
          <h4 className="mb-0 fw-bold text-gradient-primary">Edit Tool Details</h4>
          <p className="text-muted small mb-0">Update the tool record, including spec number and stock details.</p>
        </div>
        
        <div className="card-body p-4">
          {message && <div className={`alert alert-${message.type} rounded-3`}>{message.text}</div>}

          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label fw-semibold">Tool Code</label>
              <input type="text" className="form-control bg-light" required
                value={formData.toolCode} 
                onChange={(e) => setFormData({...formData, toolCode: e.target.value})} />
            </div>

            <div className="mb-3">
              <label className="form-label fw-semibold">Tool Name</label>
              <input type="text" className="form-control bg-light" required
                value={formData.toolName} 
                onChange={(e) => setFormData({...formData, toolName: e.target.value})} />
            </div>

            <div className="row g-3 mb-3">
              <div className="col-md-6">
                <label className="form-label fw-semibold">Drawing Number</label>
                <input type="text" className="form-control bg-light"
                  value={formData.drawingNumber || ''} 
                  onChange={(e) => setFormData({...formData, drawingNumber: e.target.value})} />
              </div>
              <div className="col-md-6">
                <label className="form-label fw-semibold">Spec Number</label>
                <input type="text" className="form-control bg-light"
                  value={formData.specNumber || ''} 
                  onChange={(e) => setFormData({...formData, specNumber: e.target.value})} />
                <div className="form-text text-muted">Non-unique spec numbers are allowed and may be left blank.</div>
              </div>
            </div>

            <div className="row mb-3">
              <div className="col">
                <label className="form-label fw-semibold">Min Quantity</label>
                <input type="number" className="form-control bg-light" required min="0"
                  value={formData.minimumQuantity} 
                  onChange={(e) => setFormData({...formData, minimumQuantity: e.target.value})} />
              </div>
              <div className="col">
                <label className="form-label fw-semibold text-primary">Total Stock Capacity</label>
                <input type="number" className="form-control border-primary" required min="0"
                  value={formData.totalQuantity} 
                  onChange={handleQuantityChange} />
              </div>
            </div>

            {/* 🚀 DYNAMIC SERIAL INPUTS */}
            {serials.length > 0 && (
              <div className="mb-4 p-3 border rounded bg-light">
                <label className="form-label fw-bold mb-3 text-primary">
                  Enter Physical Serial Numbers ({serials.length} Required)
                </label>
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
            )}

            <div className="row mb-4">
              <div className="col-md-6">
                <label className="form-label fw-semibold">Storage Location</label>
                <input type="text" className="form-control bg-light" required
                  value={formData.storageLocation} 
                  onChange={(e) => setFormData({...formData, storageLocation: e.target.value})} />
              </div>
              <div className="col-md-6">
                <label className="form-label fw-semibold">Status</label>
                <select className="form-select bg-light" required
                  value={formData.status}
                  onChange={(e) => setFormData({...formData, status: e.target.value})}
                >
                  <option value="AVAILABLE">AVAILABLE</option>
                  <option value="IN_USE">IN USE</option>
                  <option value="SHARPENING">SHARPENING</option>
                  <option value="DAMAGED">DAMAGED</option>
                  <option value="UNAVAILABLE">UNAVAILABLE</option>
                </select>
              </div>
            </div>

            <button type="submit" className="btn btn-primary w-100 fw-bold py-2 rounded-pill shadow-sm">
              Save Changes
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
