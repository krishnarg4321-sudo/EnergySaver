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

export const getAllAppliances = async () => {
  try {
    const response = await api.get('/appliances');
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch appliances' };
  }
};

export const getUserAppliances = async () => {
  try {
    const response = await api.get('/user-appliances');
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to fetch user appliances' };
  }
};

export const addAppliance = async (applianceId, customName, ratedWatts) => {
  try {
    const response = await api.post('/user-appliances', {
      appliance_id: applianceId,
      custom_name: customName,
      rated_watts: ratedWatts
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to add appliance' };
  }
};

export const deleteAppliance = async (userApplianceId) => {
  try {
    const response = await api.delete(`/user-appliances/${userApplianceId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: 'Failed to delete appliance' };
  }
};

export default {
  setAuthToken,
  getAllAppliances,
  getUserAppliances,
  addAppliance,
  deleteAppliance
};
