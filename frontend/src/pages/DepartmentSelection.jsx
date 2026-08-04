import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import API_URL from '../config'; 

export default function DepartmentSelection() {
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole');
  const activePlantId = localStorage.getItem('activePlantId');
  
  const [departments, setDepartments] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!userRole) {
      navigate('/login');
      return;
    }
    if (!activePlantId) {
      navigate('/plant-selection');
      return;
    }

    const fetchDepartments = async () => {
      // 🚀 CACHE CHECK: Look for saved departments first
      const cacheKey = `departments_plant_${activePlantId}`;
      const cachedData = sessionStorage.getItem(cacheKey);

      if (cachedData) {
        setDepartments(JSON.parse(cachedData));
        setIsLoading(false);
        return; // Exit early if we have cache!
      }

      try {
        const response = await axios.get(`${API_URL}/api/departments/${activePlantId}`);
        if (Array.isArray(response.data)) {
          setDepartments(response.data);
          // 🚀 SAVE CACHE: Store it for next time
          sessionStorage.setItem(cacheKey, JSON.stringify(response.data));
        } else {
          setError("Invalid data received from server.");
        }
      } catch (err) {
        console.error("Error fetching departments", err);
        setError("Failed to connect to the database.");
      } finally {
        setIsLoading(false);
      }
    };  
    
    fetchDepartments();
  }, [userRole, activePlantId, navigate]);

  const handleSelectDepartment = (deptId) => {
    localStorage.setItem('activeDeptId', deptId);
    navigate('/project-selection'); 
  };

  return (
    <div className="container-fluid bg-light min-vh-100 py-5 d-flex flex-column align-items-center">
      <div className="w-100 max-w-3xl" style={{ maxWidth: '800px' }}>
        
        <button className="btn btn-outline-secondary mb-4" onClick={() => navigate('/plant-selection')}>
          ← Back to Facilities
        </button>

        <div className="mb-5">
          <h2 className="fw-bold text-primary mb-1">Step 2: Select Department</h2>
          <span className="text-muted">Choose your department within the facility</span>
        </div>

        {error && <div className="alert alert-danger">{error}</div>}

        {isLoading ? (
          <div className="text-center py-5">
            <div className="spinner-border text-primary" role="status"></div>
            <p className="text-muted mt-2">Loading departments...</p>
          </div>
        ) : (
          <div className="row row-cols-1 row-cols-md-2 g-4">
            {departments.length === 0 ? (
              <p className="text-muted">No departments found for this facility.</p>
            ) : (
              departments.map((d) => (
                <div key={d.departmentId} className="col">
                  <div 
                    className="card h-100 border-0 shadow-sm rounded-4 p-3" 
                    style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
                    onClick={() => handleSelectDepartment(d.departmentId)}
                    onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.03)'}
                    onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
                  >
                    <div className="card-body text-center">
                      <h4 className="fw-bold text-dark mb-0">{d.departmentName}</h4>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
}
