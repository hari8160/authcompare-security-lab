# Web Security Audit Checklist (Template)

Mark each item PASS / FAIL / NOT APPLICABLE / NEEDS REVIEW based on what you actually
verify against the running application — do not mark everything PASS by default.

| Area | Item | Status | Notes |
|------|------|--------|-------|
| Authentication | Passwords hashed with BCrypt | | |
| Authentication | OAuth Authorization Code + PKCE enforced | | |
| Authorization | ADMIN-only endpoint rejects non-admin users (403) | | |
| Session management | Session cookie is HttpOnly | | |
| Session management | Session cookie has SameSite set | | |
| Session management | Session cookie has Secure flag | | Expected FAIL until HTTPS is configured |
| Password storage | No plaintext password ever persisted | | |
| OAuth implementation | `state` parameter validated | | |
| OAuth implementation | Redirect URI exact-match enforced | | |
| OAuth implementation | Authorization code single-use / short-lived | | |
| CSRF | State-changing requests protected appropriately | | |
| XSS | User input reflected safely (React escapes by default) | | |
| SQL Injection | JPA/Hibernate parameterized queries used throughout | | |
| Security headers | X-Content-Type-Options present | | |
| Security headers | X-Frame-Options / frame-ancestors present | | |
| Security headers | Referrer-Policy present | | |
| TLS | HTTPS configured (optional local cert) | | |
| Cookies | Cookie scope/path minimized | | |
| CORS | Only frontend origin allowed, credentials scoped | | |
| Input validation | DTOs validated with Bean Validation | | |
| Rate limiting | Login endpoint rate-limited | | Likely FAIL — not implemented in base project |
| Error handling | No stack traces leaked to client | | |
| Logging | No passwords/tokens in logs or DB | | |
| Secrets management | No secrets committed to source control | | |
| Dependencies | No known-vulnerable dependency versions | | Check with `mvn dependency:tree` / OWASP Dependency-Check |
| Configuration | `.env` excluded via `.gitignore` | | |
| Access control | Role checks enforced server-side, not just hidden in UI | | |
