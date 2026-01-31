import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';

// Placeholder components - to be created later
const Home = () => <div>Home Page</div>;
const Login = () => <div>Login Page</div>;
const Register = () => <div>Register Page</div>;
const Appliances = () => <div>Appliances Page</div>;
const WeeklyStats = () => <div>Weekly Stats Page</div>;
const MonthlyStats = () => <div>Monthly Stats Page</div>;
const Suggestions = () => <div>Suggestions Page</div>;

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  const { token } = useAuth();
  
  if (!token) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
};

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          {/* Protected Routes */}
          <Route 
            path="/" 
            element={
              <ProtectedRoute>
                <Home />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/appliances" 
            element={
              <ProtectedRoute>
                <Appliances />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/weekly-stats" 
            element={
              <ProtectedRoute>
                <WeeklyStats />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/monthly-stats" 
            element={
              <ProtectedRoute>
                <MonthlyStats />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/suggestions" 
            element={
              <ProtectedRoute>
                <Suggestions />
              </ProtectedRoute>
            } 
          />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
