// frontend/src/config.js
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

console.log("VITE IS USING THIS BACKEND URL: ", API_URL);

export default API_URL;
