import React from 'react';

export default function TLS() {
  return (
    <div className="container">
      <h2>SSL/TLS</h2>
      <div className="card">
        {['HTTP', 'HTTPS', 'TLS encryption (handshake, cipher negotiation)', 'Certificate validation', 'Secure communication'].map((s, i, arr) => (
          <React.Fragment key={s}>
            <div className="flow-step">{s}</div>
            {i < arr.length - 1 && <div className="flow-arrow">↓</div>}
          </React.Fragment>
        ))}
      </div>
      <div className="card">
        <p>Authentication systems should always use HTTPS in production because both traditional passwords and OAuth authorization codes/tokens are sent over the network — without TLS, any of them could be intercepted in transit.</p>
        <p className="muted">For local development, a self-signed certificate can be used to test HTTPS end-to-end, but it is only appropriate for a controlled dev/lab environment — it is <strong>not</strong> equivalent to a trusted, publicly-signed production certificate, and browsers will warn accordingly.</p>
      </div>
    </div>
  );
}
