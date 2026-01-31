import React, { createContext, useContext, useState, useEffect } from 'react';
import webSocketService from '../services/webSocketService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [alerts, setAlerts] = useState([]);

  // Handle WebSocket connection when user logs in
  useEffect(() => {
    if (user && user.userId) {
      // Connect to WebSocket and listen for alerts
      webSocketService.connect(user.userId, (alert) => {
        // Add new alert to the beginning of the array
        setAlerts(prevAlerts => [alert, ...prevAlerts]);
      });
    }

    // Cleanup: disconnect when user logs out or component unmounts
    return () => {
      if (user) {
        webSocketService.disconnect();
      }
    };
  }, [user]);

  const login = (authToken, userData) => {
    setToken(authToken);
    setUser(userData);
  };

  const logout = () => {
    webSocketService.disconnect();
    setToken(null);
    setUser(null);
    setAlerts([]);
  };

  const clearAlerts = () => {
    setAlerts([]);
  };

  const value = {
    token,
    user,
    alerts,
    login,
    logout,
    clearAlerts,
    isAuthenticated: !!token
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default AuthContext;
