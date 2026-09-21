import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css' 
import axios from 'axios';

// If a token exists from a previous session, set it on axios defaults
const savedToken = localStorage.getItem('token');
if (savedToken) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${savedToken}`;
}

axios.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const responseMessage = error.response?.data?.message || '';
    const requestUrl = error.config?.url || '';
    const isAuthRequest = requestUrl.includes('/api/auth/');
    const isSessionFailure = status === 401 && (
      responseMessage.includes('logged in on another device')
      || (!isAuthRequest && responseMessage.includes('Invalid or expired token'))
    );

    if (isSessionFailure) {
      sessionStorage.setItem(
        'authMessage',
        responseMessage.includes('logged in on another device')
          ? 'Session expired or this account was logged in on another device. Please log in again.'
          : 'Session expired. Please log in again.'
      );
      localStorage.clear();
      sessionStorage.clear();
      sessionStorage.setItem('authMessage', responseMessage.includes('logged in on another device')
        ? 'Session expired or this account was logged in on another device. Please log in again.'
        : 'Session expired. Please log in again.');
      delete axios.defaults.headers.common['Authorization'];
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
