import React, { useEffect, useState } from 'react';
import api from '../services/api';

export default function Audit() {
  const [events, setEvents] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/api/audit/events')
      .then((res) => setEvents(res.data))
      .catch(() => setError('Could not load audit events — are you logged in and is the backend running?'));
  }, []);

  return (
    <div className="container">
      <h2>Authentication Audit Log</h2>
      <p className="muted">Passwords and tokens are never recorded here — only event metadata.</p>
      {error && <div className="error">{error}</div>}
      <div className="card" style={{ overflowX: 'auto' }}>
        <table>
          <thead>
            <tr><th>Time</th><th>User ID</th><th>Method</th><th>Event</th><th>IP</th><th>Result</th></tr>
          </thead>
          <tbody>
            {events.map((e) => (
              <tr key={e.id}>
                <td>{new Date(e.timestamp).toLocaleString()}</td>
                <td>{e.userId ?? '—'}</td>
                <td><span className={`badge ${e.method === 'OAUTH' ? 'oauth' : 'password'}`}>{e.method}</span></td>
                <td>{e.eventType}</td>
                <td>{e.ipAddress}</td>
                <td><span className={`badge ${e.success ? 'success' : 'failure'}`}>{e.success ? 'SUCCESS' : 'FAILURE'}</span></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
