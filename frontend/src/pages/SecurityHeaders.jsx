import React, { useEffect, useState } from 'react';
import api from '../services/api';

export default function SecurityHeaders() {
  const [headers, setHeaders] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/api/security/headers')
      .then((res) => setHeaders(res.data))
      .catch(() => setError('Could not load header descriptions — is the backend running?'));
  }, []);

  return (
    <div className="container">
      <h2>Security Headers</h2>
      <p className="muted">These headers are applied globally by the backend's Spring Security configuration. Inspect them directly in your browser's Network tab, or in Burp/ZAP, for any response from <code>localhost:8080</code>.</p>
      {error && <div className="error">{error}</div>}
      {headers && (
        <div className="card">
          <table>
            <thead><tr><th>Header</th><th>Purpose</th></tr></thead>
            <tbody>
              {Object.entries(headers).map(([k, v]) => (
                <tr key={k}><td><code>{k}</code></td><td>{v}</td></tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
