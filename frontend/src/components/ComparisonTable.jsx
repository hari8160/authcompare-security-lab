import React from 'react';

const rows = [
  ['Primary purpose', 'Verify identity directly against the app', 'Delegate access without sharing the password'],
  ['Credentials handled by application', 'Yes — app receives raw password on every login', 'No — app never sees the provider password'],
  ['Password storage', 'App must store a hash (e.g. BCrypt) securely', 'No password stored by the app for that identity'],
  ['Token usage', 'Session token/JWT issued by the app itself', 'Access token / ID token issued by Authorization Server'],
  ['Authorization mechanism', 'App-defined roles/sessions', 'OAuth scopes define what is authorized'],
  ['Authentication server', 'The application itself', 'Separate Authorization Server (here, the LOCAL LAB server)'],
  ['Third-party login', 'Not supported natively', 'Core use case — one login across many apps'],
  ['Password reuse risk', 'High — users reuse passwords across sites', 'Low — no app-specific password to reuse'],
  ['Credential exposure', 'Password could leak from a breached app DB', 'Only tokens can leak, not the password'],
  ['Phishing risk', 'Classic password phishing pages', 'Still possible against the IdP login page, but no password shared with each app'],
  ['CSRF considerations', 'Needs explicit CSRF protection on state-changing forms', 'state parameter mitigates CSRF on the OAuth redirect'],
  ['Token leakage risk', 'Session cookie/JWT could be stolen (XSS, etc.)', 'Access/refresh tokens could be stolen if not handled carefully'],
  ['Credential revocation', 'Reset password, invalidate sessions', 'Revoke token/consent at the Authorization Server'],
  ['Scalability', 'Each app manages its own users', 'Centralized identity scales across many apps'],
  ['Implementation complexity', 'Simpler to implement initially', 'More moving parts: redirect, code exchange, PKCE, state'],
  ['User experience', 'One more password to remember', 'Fewer passwords, familiar "Sign in with X" pattern'],
  ['Best use cases', 'Simple single-app systems, internal tools', 'Multi-app ecosystems, third-party integrations, SSO'],
  ['Major security risks', 'Credential stuffing, weak/reused passwords', 'Misconfigured redirect URIs, missing PKCE/state, token theft'],
  ['Recommended protections', 'BCrypt/Argon2, MFA, rate limiting, secure cookies', 'PKCE, state, exact redirect-uri match, short-lived tokens, HTTPS'],
];

export default function ComparisonTable() {
  return (
    <div className="card" style={{ overflowX: 'auto' }}>
      <table>
        <thead>
          <tr>
            <th>Aspect</th>
            <th>Traditional Username/Password</th>
            <th>OAuth 2.0 Authorization Code</th>
          </tr>
        </thead>
        <tbody>
          {rows.map(([aspect, trad, oauth]) => (
            <tr key={aspect}>
              <td><strong>{aspect}</strong></td>
              <td>{trad}</td>
              <td>{oauth}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
