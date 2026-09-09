import React from 'react';

const traditional = [
  'Browser (Login Form)',
  'Backend: POST /api/auth/login',
  'Password Hash Verification (BCrypt)',
  'JWT issued, set as HttpOnly cookie',
  'Protected API request (cookie sent automatically)',
  'PostgreSQL (users table)',
];

const oauth = [
  'Browser: click "Sign in with LOCAL LAB OAuth"',
  'Backend: redirect to Authorization Server /oauth2/authorize (+ state, + PKCE code_challenge)',
  'Authorization Server: Login / Consent screen',
  'Redirect back with single-use, short-lived Authorization Code',
  'Backend: POST /oauth2/token (code + code_verifier) — Token Endpoint',
  'Access Token + ID Token (OIDC) returned to backend',
  'Backend fetches /userinfo, creates/links local user',
  'App session cookie issued, same as traditional flow',
  'Protected Resource (Dashboard)',
];

function FlowList({ title, steps, badgeClass }) {
  return (
    <div className="card">
      <h3>{title} <span className={`badge ${badgeClass}`}>{badgeClass.toUpperCase()}</span></h3>
      {steps.map((s, i) => (
        <React.Fragment key={s}>
          <div className="flow-step">{i + 1}. {s}</div>
          {i < steps.length - 1 && <div className="flow-arrow">↓</div>}
        </React.Fragment>
      ))}
    </div>
  );
}

export default function Flows() {
  return (
    <div className="container">
      <h2>Visual Flow Diagrams</h2>
      <FlowList title="Traditional Authentication" steps={traditional} badgeClass="password" />
      <FlowList title="OAuth 2.0 Authorization Code Flow" steps={oauth} badgeClass="oauth" />
    </div>
  );
}
