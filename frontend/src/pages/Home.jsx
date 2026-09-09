import React from 'react';
import { Link } from 'react-router-dom';

export default function Home() {
  return (
    <div className="container">
      <div className="card">
        <h1>AuthCompare Security Lab</h1>
        <p className="muted">OAuth 2.0 Authorization Code Flow vs Traditional Username/Password Authentication</p>
        <p>
          This lab lets you register and log in the traditional way (username + password,
          BCrypt-hashed, JWT session cookie) and, separately, sign in through a real local
          OAuth 2.0 / OpenID Connect Authorization Server — with PKCE, the <code>state</code>{' '}
          parameter, and single-use short-lived authorization codes — so you can compare both
          approaches directly.
        </p>
        <ul>
          <li><Link to="/register">Register</Link> / <Link to="/login">Login</Link> — traditional auth</li>
          <li><Link to="/oauth/login">OAuth Login</Link> — Authorization Code Flow against the LOCAL LAB server</li>
          <li><Link to="/comparison">Comparison table</Link></li>
          <li><Link to="/flows">Visual flow diagrams</Link></li>
          <li><Link to="/security-lab">Security Lab</Link> — password/session/OAuth security concepts</li>
          <li><Link to="/audit">Audit log</Link></li>
        </ul>
      </div>
    </div>
  );
}
