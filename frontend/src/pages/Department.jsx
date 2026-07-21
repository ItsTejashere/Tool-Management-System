import { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';

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
      const response = await axios.get(`http://localhost:8080/api/departments/${plantId}`);
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
      
      {/* Navigation / Back Button */}
      <button className="btn btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
        ← Back to Plants
      </button>

      <div className="text-center mb-5">
        <h1 className="fw-bold text-primary">DEPARTMENTS</h1>
        <h4 className="text-secondary mt-3">Select a Department</h4>
        <hr className="w-50 mx-auto" />
      </div>

      <div className="row justify-content-center">
        {departments.map((dept) => (
          <div key={dept.departmentId} className="col-md-4 mb-4">
            
            <div 
              className="card shadow-sm text-center h-100 border-0 pt-4 pb-4 cursor-pointer" 
              style={{ cursor: 'pointer', transition: 'transform 0.2s', backgroundColor: '#f8f9fa' }}
              onClick={() => handleDepartmentClick(dept.departmentId)}
              onMouseOver={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
              onMouseOut={(e) => e.currentTarget.style.transform = 'scale(1)'}
            >
              <div className="card-body">
                {/* A generic icon for departments */}
                <h1 className="text-primary mb-3">⚙️</h1> 
                <h4 className="card-title fw-bold text-dark">{dept.departmentName.toUpperCase()}</h4>
              </div>
            </div>
            
          </div>
        ))}
      </div>

    </div>
  );
}