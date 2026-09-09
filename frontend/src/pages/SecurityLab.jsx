import React from 'react';

export default function SecurityLab() {
  return (
    <div className="container">
      <h2>Security Lab</h2>

      <div className="card">
        <h3>Password Security</h3>
        <p>Example only — never a real stored credential:</p>
        <table>
          <tbody>
            <tr><td>Plaintext (never store this)</td><td><code>Sup3rSecret!</code></td></tr>
            <tr><td>BCrypt hash (what the DB actually stores)</td><td><code>$2a$12$Kx9...redacted-example...</code></td></tr>
          </tbody>
        </table>
        <p className="muted">BCrypt is adaptive (a configurable cost factor) and salts automatically per-password, so identical passwords produce different hashes and brute-forcing is deliberately slowed down.</p>
      </div>

      <div className="card">
        <h3>Session Security</h3>
        <ul>
          <li><strong>Session fixation</strong>: never reuse a pre-login session/token after authentication — issue a fresh one.</li>
          <li><strong>Session expiration</strong>: this lab's JWT expires after 1 hour (<code>app.jwt.expiration-ms</code>).</li>
          <li><strong>HttpOnly</strong>: prevents JavaScript from reading the session cookie, mitigating token theft via XSS.</li>
          <li><strong>SameSite</strong>: set to <code>Lax</code> here, restricting when the cookie is sent cross-site.</li>
          <li><strong>Secure flag</strong>: should be <code>true</code> once served over HTTPS (see /tls) so the cookie is never sent in plaintext.</li>
        </ul>
      </div>

      <div className="card">
        <h3>OAuth Security</h3>
        <ul>
          <li><strong>Authorization-code interception</strong>: mitigated here by requiring PKCE even for this confidential client.</li>
          <li><strong>CSRF / state attacks</strong>: the <code>state</code> parameter ties the callback to the request that started it.</li>
          <li><strong>Redirect URI manipulation</strong>: the Authorization Server only accepts the exact, pre-registered redirect URI.</li>
          <li><strong>Token leakage</strong>: tokens are handled server-side and never placed in the URL or exposed to frontend JavaScript.</li>
          <li><strong>Insufficient scope</strong>: this client only requests <code>openid profile email</code> — least privilege.</li>
        </ul>
      </div>

      <div className="card">
        <p className="muted">Any deliberately vulnerable configurations for demonstration purposes are isolated to this local lab and clearly labeled — never deploy or expose this project as-is.</p>
      </div>
    </div>
  );
}
