import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function Dashboard() {
  const navigate = useNavigate();
  const [plants, setPlants] = useState([]); 

  const fetchPlants = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/plants');
      setPlants(response.data);
    } catch (error) {
      console.error("Error fetching plants", error);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    fetchPlants();
  }, []);

  const handlePlantClick = (plantId) => {
    navigate(`/departments/${plantId}`);
  };

  return (
    <div className="container mt-5">
      <div className="text-center mb-5">
      <img 
            src="./src/assets/et.png" 
            alt="TMS Logo" 
            className="mb-3"
            style={{ maxWidth: '150px' }} 
            onError={(e) => e.target.src = "https://via.placeholder.com/150?text=Logo"}
          />
        <h4 className="text-secondary mt-3">Select Company</h4>
        <hr className="w-50 mx-auto" />
      </div>

      <div className="row justify-content-center">
        {plants.map((plant) => (
          <div key={plant.plantId} className="col-md-4 mb-4">
            <div 
              className="card shadow-sm text-center h-100 border-0 pt-4 cursor-pointer" 
              style={{ cursor: 'pointer', transition: 'transform 0.2s' }}
              onClick={() => handlePlantClick(plant.plantId)}
              onMouseOver={(e) => e.currentTarget.style.transform = 'scale(1.05)'}
              onMouseOut={(e) => e.currentTarget.style.transform = 'scale(1)'}
            >
              <div className="card-body">
                <img 
                  src={`/${plant.imageName}`} 
                  alt={plant.plantName} 
                  style={{ maxHeight: '120px', maxWidth: '100%' }}
                  className="mb-4"
                  onError={(e) => e.target.src = "https://via.placeholder.com/150x120?text=Factory"}
                />
                <h4 className="card-title fw-bold text-dark">{plant.plantName.toUpperCase()}</h4>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}