import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function EditTool() {
  const { id } = useParams(); // Grabs the tool ID from the URL
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole');

  // Security Check
  useEffect(() => {
    if (userRole !== 'INVENTORY') navigate('/dashboard');
  }, [userRole, navigate]);

  const [formData, setFormData] = useState({
    toolCode: '',
    toolName: '',
    minimumQuantity: '',
    totalQuantity: '',
    storageLocation: '',
    status: ''
  });

  const [message, setMessage] = useState(null);

  // FETCH the existing tool data when the page loads
  useEffect(() => {
    const fetchToolDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/tools/${id}`);
        setFormData(response.data);
      } catch (error) {
        setMessage({ type: 'danger', text: 'Error loading tool details.' });
      }
    };
    fetchToolDetails();
  }, [id]);

  // SAVE the changes
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.put(`http://localhost:8080/api/tools/${id}`, formData);
      if (response.data.status === true) {
        setMessage({ type: 'success', text: response.data.message });
        setTimeout(() => navigate('/dashboard'), 1000);
      }
    } catch (error) {
      setMessage({ type: 'danger', text: 'Error updating tool.' });
    }
  };

  return (
    <div className="container mt-5">
      <button className="btn btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
        ← Back to Dashboard
      </button>

      <div className="card shadow-sm border-0 mx-auto" style={{ maxWidth: '600px' }}>
        <div className="card-header bg-white py-3">
          <h4 className="mb-0 fw-bold text-primary">Edit Tool Details</h4>
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

            <div className="row mb-3">
              <div className="col">
                <label className="form-label fw-semibold">Min Quantity</label>
                <input type="number" className="form-control bg-light" required min="0"
                  value={formData.minimumQuantity} 
                  onChange={(e) => setFormData({...formData, minimumQuantity: e.target.value})} />
              </div>
              <div className="col">
                <label className="form-label fw-semibold">Total Stock Capacity</label>
                <input type="number" className="form-control bg-light" required min="1"
                  value={formData.totalQuantity} 
                  onChange={(e) => setFormData({...formData, totalQuantity: e.target.value})} />
              </div>
            </div>

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