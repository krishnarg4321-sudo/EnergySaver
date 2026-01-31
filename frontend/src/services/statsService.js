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

export const getWeeklyStats = async () => {
  try {
    const response = await api.get('/stats/weekly');
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch weekly stats' };
  }
};

export const getMonthlyStats = async () => {
  try {
    const response = await api.get('/stats/monthly');
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch monthly stats' };
  }
};

export default {
  setAuthToken,
  getWeeklyStats,
  getMonthlyStats
};
