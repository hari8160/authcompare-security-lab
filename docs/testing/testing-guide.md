# Security Testing Guide (Local Lab Only)

All steps target `http://localhost:3000` and `http://localhost:8080` (and `http://localhost:9000`
for the Authorization Server). Never point these tools at external systems.

## 1. Burp Suite

1. Start the full stack (Docker or manual) so all three services are running.
2. Configure your browser to proxy through Burp (default `127.0.0.1:8080` proxy port —
   change Burp's proxy listener port if it clashes with the backend's port 8080, e.g. use 8081).
3. Open Burp Suite, go to **Proxy → Intercept**, turn intercept **On**.
4. In the proxied browser, visit `http://localhost:3000`.
5. Go to `/login`, submit the traditional login form. Capture the `POST /api/auth/login` request.
6. Inspect the request: note the JSON body (username/password) and headers.
7. Inspect the response: note the `Set-Cookie: AUTHLAB_SESSION=...` header, and confirm
   `HttpOnly` is present (Burp's Proxy → HTTP history shows cookie attributes).
8. Go to `/oauth/login`, click "Sign in with LOCAL LAB OAuth". Capture the sequence of
   requests: the redirect to `/oauth2/authorize`, the login/consent POST on `:9000`,
   the redirect back to `/login/oauth2/code/local-lab?code=...&state=...`, and the
   server-to-server token exchange (this last one happens backend↔oauth-server and
   won't appear in your browser-proxied traffic — note that as an observation).
9. Compare: the traditional login sends the password directly in the request body;
   the OAuth flow never sends a password to this application at all — only a code parameter.
10. Turn Intercept back **Off** when done to avoid stalling your own browsing.

## 2. OWASP ZAP

1. Set Target to `http://localhost:8080` (backend) — do not scan anything else.
2. Run a **Spider** or manually browse the app through ZAP's proxy to populate the site tree.
3. Run a **Passive Scan** (default) and review alerts for:
   - Missing/misconfigured security headers (compare against `/api/security/headers` and `/security-headers` page)
   - Cookie configuration (`HttpOnly`, `SameSite`, `Secure` — `Secure` will correctly flag as missing until HTTPS is configured)
   - Information disclosure in error responses (should be minimal — see `GlobalExceptionHandler`)
4. Optionally run an **Active Scan** against `http://localhost:8080` only, in a lab-only
   context, to check for reflected input handling and basic access-control issues
   (e.g. try accessing `/api/admin/users` as a non-admin session — expect `403`).
5. Document each finding as PASS/FAIL/NOT APPLICABLE/NEEDS REVIEW using the audit
   checklist in `docs/report/web-security-audit-checklist.md`.
6. Flag expected "findings" that are not real vulnerabilities for a dev-mode lab
   (e.g. missing `Secure` cookie flag while running on plain HTTP) as such, with reasoning.

## 3. Nmap (localhost only)

```bash
nmap -sV -p 3000,3306,8080,9000 127.0.0.1
```

- Confirm exactly the four expected services are listening: React dev server (3000),
  MySQL (3306), backend API (8080), and OAuth Authorization Server (9000).
- If MySQL (3306) is unintentionally reachable from outside your machine when
  using Docker, that's worth flagging as a finding — restrict the port binding to
  `127.0.0.1:3306:3306` in `docker-compose.yml` for a stricter lab configuration.
- Do not scan any address other than `127.0.0.1` / `localhost`.

## Notes for your report

For each finding from Burp/ZAP/Nmap, record it in the Vulnerability Assessment table
(`docs/report/vulnerability-assessment.md`) — only include what you actually observed,
not hypothetical issues.
