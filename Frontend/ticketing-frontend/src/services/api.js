import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = {
  // Configuration
  saveConfiguration: async (config) => {
    const response = await axios.post(`${API_URL}/configuration`, config);
    return response.data;
  },
  
  getConfiguration: async () => {
    const response = await axios.get(`${API_URL}/configuration`);
    return response.data;
  },

  // Ticket Operations
  getTicketStatus: async () => {
    const response = await axios.get(`${API_URL}/tickets/status`);
    return response.data;
  },

  // Logs
  getLogs: async () => {
    const response = await axios.get(`${API_URL}/logs`);
    return response.data;
  },

  getLogsByType: async (type) => {
    const response = await axios.get(`${API_URL}/logs/${type}`);
    return response.data;
  },

  // System Health
  getSystemHealth: async () => {
    const response = await axios.get(`${API_URL}/system/health`);
    return response.data;
  }
};

export default api;