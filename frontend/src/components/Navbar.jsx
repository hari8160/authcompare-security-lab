import React from 'react';
import { NavLink } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

const links = [
  ['/', 'Home'],
  ['/login', 'Login'],
  ['/register', 'Register'],
  ['/oauth/login', 'OAuth Login'],
  ['/dashboard', 'Dashboard'],
  ['/comparison', 'Comparison'],
  ['/flows', 'Flows'],
  ['/security-lab', 'Security Lab'],
  ['/security-headers', 'Headers'],
  ['/tls', 'TLS'],
  ['/audit', 'Audit'],
  ['/admin/users', 'Admin'],
];

export default function Navbar() {
  const { user, logout } = useAuth();
  return (
    <div className="navbar">
      <div className="brand">
        AuthCompare Security Lab
        <small>OAuth 2.0 Auth Code Flow vs Traditional Auth</small>
      </div>
      {links.map(([to, label]) => (
        <NavLink key={to} to={to} className={({ isActive }) => (isActive ? 'active' : '')}>
          {label}
        </NavLink>
      ))}
      {user ? (
        <button onClick={logout} style={{ marginLeft: 'auto' }}>Logout ({user.username})</button>
      ) : (
        <span className="muted" style={{ marginLeft: 'auto' }}>Not signed in</span>
      )}
    </div>
  );
}
