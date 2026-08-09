import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import API_URL from '../config';

export default function Department() {

  const [departments, setDepartments] = useState([]);
  const { plantId } = useParams(); // Extracts the '1' from http://localhost:5173/departments/1
  const navigate = useNavigate();

  useEffect(() => {
    // eslint-disable-next-line react-hooks/immutability
    fetchDepartments();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [plantId]); // Re-run if the URL changes

  const fetchDepartments = async () => {
    try {
      // Send the specific plantId to your Java API
      const response = await axios.get(`${API_URL}/api/departments/${plantId}`);
      setDepartments(response.data);
    } catch (error) {
      console.error("Error fetching departments", error);
    }
  };

  const handleDepartmentClick = (deptId) => {
    navigate(`/machines/${deptId}`); 
  };

  return (
    <div className="container mt-5">
      
      <button className="btn btn-outline-secondary mb-4 shadow-sm" onClick={() => navigate('/dashboard')}>
        ← Back to Plants
      </button>

      <div className="text-center mb-5">
        <h1 className="fw-bold text-gradient-primary">Departments</h1>
        <p className="text-muted mt-3">Choose the department to continue your tool workflow.</p>
      </div>

      <div className="row justify-content-center g-4">
        {departments.map((dept) => (
          <div key={dept.departmentId} className="col-md-4 mb-4">
            
            <div 
              className="card panel-card text-center h-100 border-0 pt-4 pb-4" 
              style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
              onClick={() => handleDepartmentClick(dept.departmentId)}
              onMouseOver={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
              onMouseOut={(e) => e.currentTarget.style.transform = 'scale(1)'}
            >
              <div className="card-body">
                <div className="mb-3 display-6">⚙️</div>
                <h4 className="card-title fw-bold text-dark">{dept.departmentName.toUpperCase()}</h4>
              </div>
            </div>
            
          </div>
        ))}
      </div>

    </div>
  );
}