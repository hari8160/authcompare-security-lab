import React from 'react';
import ComparisonTable from '../components/ComparisonTable';

export default function Comparison() {
  return (
    <div className="container">
      <h2>Comparison: Traditional Auth vs OAuth 2.0 Authorization Code</h2>
      <p className="muted">
        Reminder: OAuth 2.0 is an <strong>authorization</strong> framework, not an authentication
        protocol by itself. Identity ("who is this user") comes from OpenID Connect layered on
        top of OAuth 2.0 — here, the ID token and userinfo endpoint.
      </p>
      <ComparisonTable />
    </div>
  );
}
