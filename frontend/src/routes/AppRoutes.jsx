import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from '../pages/Home';
import Register from '../pages/Register';
import Login from '../pages/Login';
import OAuthLogin from '../pages/OAuthLogin';
import OAuthCallback from '../pages/OAuthCallback';
import Dashboard from '../pages/Dashboard';
import Comparison from '../pages/Comparison';
import Flows from '../pages/Flows';
import SecurityLab from '../pages/SecurityLab';
import SecurityHeaders from '../pages/SecurityHeaders';
import TLS from '../pages/TLS';
import Audit from '../pages/Audit';
import AdminUsers from '../pages/AdminUsers';
import ProtectedRoute from '../components/ProtectedRoute';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/oauth/login" element={<OAuthLogin />} />
      <Route path="/oauth/callback" element={<OAuthCallback />} />
      <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/comparison" element={<Comparison />} />
      <Route path="/flows" element={<Flows />} />
      <Route path="/security-lab" element={<SecurityLab />} />
      <Route path="/security-headers" element={<SecurityHeaders />} />
      <Route path="/tls" element={<TLS />} />
      <Route path="/audit" element={<ProtectedRoute><Audit /></ProtectedRoute>} />
      <Route path="/admin/users" element={<ProtectedRoute requireAdmin><AdminUsers /></ProtectedRoute>} />
    </Routes>
  );
}
