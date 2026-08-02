import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import API_URL from '../config'; // Pulls the URL safely from your updated config
import logoImage from '../assets/etgp.jpg'; // Fixes the infinite loading loop!

export default function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage(''); // Clear any previous messages

    try {
      // 🚀 FIXED: Pointing exactly to your Spring Boot AuthController mapping
      const response = await axios.post(`${API_URL}/api/auth/login`, {
        username: username,
        password: password
      });

      if (response.data.status === true) {
        setMessage('Success: ' + response.data.message);
        
        // Save the user role from your backend response
        localStorage.setItem('userRole', response.data.role);

        // Redirect to dashboard after a short delay
        setTimeout(() => {
          navigate('/dashboard');
        }, 1000);
      } else {
        setMessage('Error: Invalid Credentials');
      }
    } catch (error) {
      console.error("Login Error details:", error);
      
      // Handle the 401 Unauthorized from your backend Failure Format
      if (error.response && error.response.status === 401) {
          setMessage('Error: Invalid Credentials');
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
            src={logoImage}
            alt="ETGP Logo"
            className="mb-3"
            style={{ maxWidth: '150px' }}
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
                className="form-control form-control-lg bg-light border-0 border-end-0"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                className="btn bg-light border-0 text-primary fw-semibold"
                type="button"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>
          </div>

          <button type="submit" className="btn btn-primary btn-lg w-100 fw-bold rounded-pill shadow-sm">
            Sign In
          </button>
        </form>
      </div>
    </div>
  );
}
