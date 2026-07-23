import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import axios from 'axios';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState(''); 
  const [showPassword, setShowPassword] = useState(false); 
  const navigate = useNavigate();

 const handleLogin = async (e) => {
    e.preventDefault(); 
    try {
      // Note the new /auth/ URL
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username: username,
        password: password
      });
      
      if (response.data.status === true) {
        // Save the role to local storage for the dashboard to use later
        localStorage.setItem('userRole', response.data.role); 
        setMessage("Success: " + response.data.message);
        setTimeout(() => {
          navigate('/plant-selection'); // Move to Step 1
        }, 500);
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setMessage("Error: " + error.response.data.message); // Reads the new failure message
      } else {
        setMessage("Error: Cannot connect to server");
      }
    }
  };

  return (
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100 bg-light">
      <div className="card shadow-lg p-5 border-0 rounded-4" style={{ width: '450px', backgroundColor: '#ffffff' }}>
        
        <div className="text-center mb-4">
          <img 
            src="./src/assets/et.png" 
            alt="TMS Logo" 
            className="mb-3"
            style={{ maxWidth: '150px' }} 
            onError={(e) => e.target.src = "https://via.placeholder.com/150?text=Logo"}
          />
          <p className="text-muted">Welcome to Tool Management System.</p>
        </div>
        
        {message && (
          <div className={`alert ${message.startsWith('Success') ? 'alert-success' : 'alert-danger'} rounded-3`}>
            {message}
          </div>
        )}

        <form onSubmit={handleLogin}>
          <div className="mb-4">
            <label className="form-label fw-semibold text-secondary">Username</label>
            <input 
              type="text" 
              className="form-control form-control-lg bg-light border-0" 
              placeholder="Enter your username"
              value={username}
              onChange={(e) => setUsername(e.target.value)} 
              required 
            />
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold text-secondary">Password</label>
            <div className="input-group">
              <input 
                type={showPassword ? "text" : "password"} 
                className="form-control form-control-lg bg-light border-0" 
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)} 
                required 
              />
              <button 
                type="button" 
                className="btn btn-light border-0 text-primary fw-bold"
                onClick={() => setShowPassword(!showPassword)} 
                style={{ backgroundColor: '#f8f9fa' }} 
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>
          </div>

          <button type="submit" className="btn btn-primary btn-lg w-100 rounded-pill fw-bold shadow-sm mt-3">
            Sign In
          </button>
        </form>
      </div>
    </div>
  );
}