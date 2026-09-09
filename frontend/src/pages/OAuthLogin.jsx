import React from 'react';
import { startOAuthLogin } from '../services/oauthService';

export default function OAuthLogin() {
  return (
    <div className="container">
      <div className="card" style={{ maxWidth: 520 }}>
        <h2>OAuth 2.0 Login <span className="badge oauth">OAUTH</span></h2>
        <p className="muted">
          This performs a real redirect-based Authorization Code Flow against the
          <strong> LOCAL LAB OAuth Authorization Server</strong> running at
          <code> http://localhost:9000</code>. You will be redirected there to log in
          and consent, then redirected back here with an authorization code that the
          backend exchanges for tokens — your credentials are never seen by this app.
        </p>
        <ol className="muted">
          <li>Browser is redirected to the backend's authorization request endpoint</li>
          <li>Backend redirects to the Authorization Server with <code>state</code> + PKCE <code>code_challenge</code></li>
          <li>You log in / consent on the Authorization Server</li>
          <li>Authorization Server redirects back with a single-use, short-lived code</li>
          <li>Backend exchanges the code (+ PKCE <code>code_verifier</code>) for tokens</li>
          <li>Backend creates your app session and redirects you to the dashboard</li>
        </ol>
        <button className="secondary" onClick={startOAuthLogin}>Sign in with LOCAL LAB OAuth</button>
      </div>
    </div>
  );
}
