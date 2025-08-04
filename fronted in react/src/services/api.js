import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Update if your backend runs on a different port
});

export default api;
