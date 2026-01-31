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

export const getUserAlerts = async () => {
  try {
    const response = await api.get('/alerts');
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch alerts' };
  }
};

export const markAlertAsRead = async (alertId) => {
  try {
    const response = await api.patch(`/alerts/${alertId}/read`);
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to mark alert as read' };
  }
};

export const respondToAlert = async (alertId, userResponse, adjustmentPercentage) => {
  try {
    const response = await api.post(`/alerts/${alertId}/respond`, {
      user_response: userResponse,
      adjustment_percentage: adjustmentPercentage
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to respond to alert' };
  }
};

export default {
  setAuthToken,
  getUserAlerts,
  markAlertAsRead,
  respondToAlert
};
