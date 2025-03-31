// src/App.js
import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

// Components
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import LocationsList from './components/locations/LocationsList';
import LocationForm from './components/locations/LocationForm';
import TransportationsList from './components/transportations/TransportationsList';
import TransportationForm from './components/transportations/TransportationForm';
import RouteSearch from './components/routes/RouteSearch';

// Services
import AuthService from './services/AuthService';

// Protected route component for admin-only routes
const AdminRoute = ({ children }) => {
  const isAdmin = AuthService.isAdmin();

  if (!isAdmin) {
    return <Navigate to="/routes" />;
  }

  return children;
};

// Protected route component for authenticated routes
const ProtectedRoute = ({ children }) => {
  const user = AuthService.getCurrentUser();

  if (!user) {
    return <Navigate to="/login" />;
  }

  return children;
};

function App() {
  useEffect(() => {
    // Initialize authentication state from localStorage
    AuthService.initializeAuth();
    // Setup axios interceptors for handling auth errors
    AuthService.setupAxiosInterceptors();
  }, []);

  return (
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />

          <Route path="/" element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }>
            {/* Admin-only routes */}
            <Route path="locations" element={
              <AdminRoute>
                <LocationsList />
              </AdminRoute>
            } />
            <Route path="locations/add" element={
              <AdminRoute>
                <LocationForm />
              </AdminRoute>
            } />
            <Route path="locations/edit/:id" element={
              <AdminRoute>
                <LocationForm />
              </AdminRoute>
            } />

            <Route path="transportations" element={
              <AdminRoute>
                <TransportationsList />
              </AdminRoute>
            } />
            <Route path="transportations/add" element={
              <AdminRoute>
                <TransportationForm />
              </AdminRoute>
            } />
            <Route path="transportations/edit/:id" element={
              <AdminRoute>
                <TransportationForm />
              </AdminRoute>
            } />

            {/* Route accessible to all authenticated users */}
            <Route path="routes" element={<RouteSearch />} />

            {/* Default redirect */}
            <Route path="" element={<Navigate to="/routes" />} />
          </Route>

          {/* Catch-all redirect */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </Router>
  );
}

export default App;
