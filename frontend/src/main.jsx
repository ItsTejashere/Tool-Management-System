import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css' 
import axios from 'axios';
import API_URL from './config';

// If a token exists from a previous session, set it on axios defaults
const savedToken = localStorage.getItem('token');
if (savedToken) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${savedToken}`;
}

const sessionChannel = typeof BroadcastChannel !== 'undefined'
  ? new BroadcastChannel('tms-session')
  : null;

const redirectToLogin = (reason) => {
  localStorage.clear();
  sessionStorage.clear();
  delete axios.defaults.headers.common['Authorization'];
  window.location.href = `/login?reason=${reason}`;
};

if (sessionChannel) {
  sessionChannel.addEventListener('message', (event) => {
    if (event.data?.type === 'session-replaced' && localStorage.getItem('token')) {
      redirectToLogin('session-conflict');
    }
  });
}

window.setInterval(() => {
  if (localStorage.getItem('token')) {
    axios.get(`${API_URL}/api/session/validate`).catch(() => {});
  }
}, 5000);

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
      const reason = responseMessage.includes('logged in on another device')
        ? 'session-conflict'
        : 'session-expired';
      redirectToLogin(reason);
    }
    return Promise.reject(error);
  }
);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
