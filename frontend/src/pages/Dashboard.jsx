import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function Dashboard() {
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole'); 
  const activeProjectId = localStorage.getItem('activeProjectId');
  const activeProjectName = localStorage.getItem('activeProjectName');

  const [tools, setTools] = useState([]); 
  const [searchTerm, setSearchTerm] = useState('');

  // Fetch live data & Check Security/Project State
  useEffect(() => {
    if (!userRole) {
      navigate('/login');
      return; 
    }
    if (!activeProjectId) {
      navigate('/project-selection'); // Kick back to selection if no project is active
      return;
    }
    
    const fetchTools = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/tools');
        setTools(response.data);
      } catch (error) {
        console.error("Error fetching tools", error);
      }
    };

    fetchTools();
  }, [userRole, activeProjectId, navigate]);

  const handleLogout = () => {
    localStorage.clear(); // Clears everything including project data
    navigate('/login');
  };

  const handleChangeProject = () => {
    localStorage.removeItem('activeProjectId');
    localStorage.removeItem('activeProjectName');
    navigate('/project-selection');
  };

  // Just for your reference, this is already in your Dashboard.jsx!
const handleDelete = async (toolId) => {
  if (window.confirm("Are you sure you want to delete this tool? This will also erase its serial numbers and movement history!")) {
    try {
      const response = await axios.delete(`http://localhost:8080/api/tools/${toolId}`);
      if (response.data.status === true) {
        // Instantly remove it from the screen without refreshing
        setTools(tools.filter(tool => tool.toolId !== toolId));
      }
    } catch (error) {
      alert("Error: Could not delete tool.");
    }
  }
};

  // --- 1. FILTER BY ACTIVE PROJECT ---
  const projectTools = tools.filter(tool => tool.projectId === parseInt(activeProjectId));

  // --- 2. DYNAMIC METRICS ---
  const totalTools = projectTools.length;
  const totalAvailableStock = projectTools.reduce((sum, tool) => sum + (tool.availableQuantity || 0), 0);
  const lowStockItems = projectTools.filter(tool => tool.availableQuantity <= tool.minimumQuantity).length;
  const sharpeningItems = projectTools.filter(tool => tool.status === 'SHARPENING').length;
  const damagedItems = projectTools.filter(tool => tool.status === 'DAMAGED').length;

  // --- 3. SEARCH FILTER ---
  const displayTools = projectTools.filter(tool => 
    tool.toolCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
    tool.toolName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container-fluid bg-light min-vh-100 py-4">
      
      {/* Header */}
      <div className="d-flex justify-content-between align-items-center mb-4 px-3">
        <div>
          <h2 className="fw-bold text-primary mb-0">{activeProjectName} Inventory</h2>
          <span className="text-muted small">TMS Dashboard</span>
        </div>
        <div className="d-flex align-items-center gap-3">
          <span className="fw-semibold text-secondary border border-secondary px-3 py-1 rounded-pill">
            Role: {userRole}
          </span>
          <button className="btn btn-outline-secondary btn-sm rounded-pill px-3 fw-bold" onClick={handleChangeProject}>
            Change Project
          </button>
          <button className="btn btn-outline-danger btn-sm rounded-pill px-4 fw-bold" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>

      {/* Metrics Cards */}
      <div className="row px-3 mb-4">
        {[
          { title: 'Total Tool Types', value: totalTools, color: 'primary' },
          { title: 'Total Available Stock', value: totalAvailableStock, color: 'success' },
          { title: 'Low Stock Alerts', value: lowStockItems, color: 'danger' },
          { title: 'In Sharpening', value: sharpeningItems, color: 'warning' },
          { title: 'Damaged Tools', value: damagedItems, color: 'secondary' }
        ].map((metric, index) => (
          <div key={index} className="col">
            <div className={`card border-0 shadow-sm border-start border-${metric.color} border-4 h-100`}>
              <div className="card-body py-3">
                <p className="text-muted fw-semibold mb-1" style={{ fontSize: '0.85rem' }}>{metric.title.toUpperCase()}</p>
                <h3 className={`fw-bold text-${metric.color} mb-0`}>{metric.value}</h3>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Tool Table */}
      <div className="card shadow-sm border-0 mx-3 rounded-3 overflow-hidden">
        <div className="card-header bg-white d-flex justify-content-between align-items-center py-3 border-bottom-0">
          <h5 className="mb-0 fw-bold text-dark">Tool Inventory</h5>
          <div className="d-flex gap-3">
            <input 
              type="text" 
              className="form-control bg-light border-0" 
              placeholder="Search tools by code..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{ width: '250px' }}
            />
            {userRole === 'INVENTORY' && (
              <button 
                className="btn btn-primary fw-bold text-nowrap rounded-3 shadow-sm"
                onClick={() => navigate('/add-tool')} 
              >
                + Add Tool
              </button>
            )}
          </div>
        </div>

        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light text-secondary" style={{ fontSize: '0.9rem' }}>
              <tr>
                <th className="ps-4">Code</th>
                <th>Tool Name</th>
                <th>Min Qty</th>
                <th>Available</th>
                <th>Status</th>
                {userRole === 'INVENTORY' && <th className="text-end pe-4">Actions</th>}
              </tr>
            </thead>
            <tbody>
              {displayTools.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center py-5 text-muted">
                    No tools found for this project.
                  </td>
                </tr>
              ) : (
                displayTools.map((tool) => {
                  const isLowStock = tool.availableQuantity <= tool.minimumQuantity;

                  return (
                    <tr key={tool.toolId}>
                      <td className="ps-4 fw-semibold">{tool.toolCode}</td>
                      <td>{tool.toolName}</td>
                      <td className="text-secondary fw-semibold">{tool.minimumQuantity}</td>
                      <td>
                        <span className={`badge ${!isLowStock ? 'bg-success' : 'bg-danger'} rounded-pill px-3`}>
                          {tool.availableQuantity}
                        </span>
                      </td>
                      <td>
                        <span className={`text-${tool.status === 'AVAILABLE' ? 'success' : tool.status === 'DAMAGED' ? 'danger' : 'warning'} fw-semibold`} style={{ fontSize: '0.85rem' }}>
                          ● {tool.status.replace('_', ' ')}
                        </span>
                      </td>
                      
                      {userRole === 'INVENTORY' && (
                        <td className="text-end pe-4">
                          <button className="btn btn-sm btn-primary fw-bold me-2 shadow-sm" onClick={() => navigate(`/tool/${tool.toolId}`)}>Manage</button>
                          <button className="btn btn-sm btn-light text-primary me-2 fw-bold" onClick={() => navigate(`/edit-tool/${tool.toolId}`)}>Edit</button>
                          <button className="btn btn-sm btn-light text-danger fw-bold" onClick={() => handleDelete(tool.toolId)}>Delete</button>
                        </td>
                      )}
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}