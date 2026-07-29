import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function PlantSelection() {
  const navigate = useNavigate();
  const userRole = localStorage.getItem('userRole');
  const [plants, setPlants] = useState([]);

  useEffect(() => {
    if (!userRole) {
      navigate('/login');
      return;
    }
    const fetchPlants = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/plants');
        setPlants(response.data);
      } catch (error) {
        console.error("Error fetching plants", error);
      }
    };
    fetchPlants();
  }, [userRole, navigate]);

  const handleSelectPlant = (plantId) => {
    localStorage.setItem('activePlantId', plantId);
    navigate('/department-selection'); // Move to Step 2
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/login');
  };

  return (
    <div className="container-fluid bg-light min-vh-100 py-5 d-flex flex-column align-items-center">
      <div className="w-100" style={{ maxWidth: '900px' }}>
        <div className="d-flex justify-content-between align-items-center mb-5">
          <div>
  
            <h2 className="fw-bold text-primary mb-1">Select Facility</h2>

          </div>
          <button className="btn btn-outline-danger btn-sm rounded-pill px-4 fw-bold" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <div className="row row-cols-1 row-cols-md-2 g-4">
          {plants.map(p => (
            <div key={p.plantId} className="col">
              <div 
                className="card h-100 border-0 rounded-4 overflow-hidden shadow-sm"
                style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
                onClick={() => handleSelectPlant(p.plantId)}
                onMouseEnter={(e) => e.currentTarget.style.transform = 'scale(1.02)'}
                onMouseLeave={(e) => e.currentTarget.style.transform = 'scale(1)'}
              >
                <img 
                  src={`/${p.imageName}`} 
                  alt={p.plantName} 
                  className="card-img-top p-3" 
                  style={{ height: '200px', objectFit: 'contain', backgroundColor: '#ffffff' }}
                  onError={(e) => { e.target.src = '/default-plant.png' }}
                />
                <div className="card-body text-center py-4">
                  <h4 className="fw-bold text-dark mb-1">{p.plantName}</h4>
                  <p className="text-muted mb-0"><i className="bi bi-geo-alt-fill me-2"></i>{p.location}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}