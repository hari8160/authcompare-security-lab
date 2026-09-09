import React, { useEffect, useState } from 'react';
import api from '../services/api';

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/api/admin/users')
      .then((res) => setUsers(res.data))
      .catch((err) => setError(err.response?.status === 403 ? '403 — ADMIN role required.' : 'Could not load users.'));
  }, []);

  return (
    <div className="container">
      <h2>Admin: All Users</h2>
      <p className="muted">Authentication answers "who are you?". Authorization (this ADMIN-only page) answers "what are you allowed to access?".</p>
      {error && <div className="error">{error}</div>}
      <div className="card" style={{ overflowX: 'auto' }}>
        <table>
          <thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Role</th><th>Auth Source</th><th>Enabled</th></tr></thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.id}</td><td>{u.username}</td><td>{u.email}</td>
                <td>{u.role}</td>
                <td><span className={`badge ${u.authSource === 'OAUTH' ? 'oauth' : 'password'}`}>{u.authSource}</span></td>
                <td>{String(u.enabled)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
