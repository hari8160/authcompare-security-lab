import React from 'react';
import useAuth from '../hooks/useAuth';

export default function Dashboard() {
  const { user } = useAuth();
  if (!user) return null;

  return (
    <div className="container">
      <div className="card">
        <h2>Protected Dashboard</h2>
        <p>Welcome, <strong>{user.username}</strong>.</p>
        <table>
          <tbody>
            <tr><td>Email</td><td>{user.email}</td></tr>
            <tr><td>Role</td><td>{user.role}</td></tr>
            <tr><td>Enabled</td><td>{String(user.enabled)}</td></tr>
            <tr>
              <td>Authenticated via</td>
              <td><span className={`badge ${user.authSource === 'OAUTH' ? 'oauth' : 'password'}`}>{user.authSource}</span></td>
            </tr>
          </tbody>
        </table>
        <p className="muted">This same protected route works identically whether you signed in via traditional login or OAuth — both issue the same kind of app session cookie behind the scenes.</p>
      </div>
    </div>
  );
}
