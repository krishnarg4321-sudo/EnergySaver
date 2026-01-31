import axios from 'axios';

const API_URL = '/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add token to requests
let authToken = null;

export const setAuthToken = (token) => {
  authToken = token;
};

api.interceptors.request.use(
  (config) => {
    if (authToken) {
      config.headers.Authorization = `Bearer ${authToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const logConsumption = async (userApplianceId, logDate, hoursUsed) => {
  try {
    const response = await api.post('/consumption', {
      user_appliance_id: userApplianceId,
      log_date: logDate,
      hours_used: hoursUsed
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to log consumption' };
  }
};

export const getUserConsumption = async (startDate, endDate) => {
  try {
    const params = {};
    if (startDate) params.start_date = startDate;
    if (endDate) params.end_date = endDate;
    
    const response = await api.get('/consumption', { params });
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch consumption data' };
  }
};

export default {
  setAuthToken,
  logConsumption,
  getUserConsumption
};
