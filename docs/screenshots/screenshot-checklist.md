# Screenshot Checklist

Take these yourself while the stack is running (`docker compose up` or manual start).
Save each as `docs/screenshots/NN-description.png`.

1. Application homepage (`http://localhost:3000/`)
2. Traditional registration form filled in, before submit (`/register`)
3. Traditional login form (`/login`)
4. Authenticated dashboard after traditional login (`/dashboard`, badge shows PASSWORD)
5. OAuth login initiation page (`/oauth/login`)
6. OAuth Authorization Server's login/consent screen (`localhost:9000`, mid-flow)
7. Browser URL bar showing the callback with `?code=...&state=...` right after redirect
8. Authenticated dashboard after OAuth login (badge shows OAUTH)
9. Comparison table page (`/comparison`)
10. Traditional authentication flow diagram (`/flows`, top section)
11. OAuth authorization-code flow diagram (`/flows`, bottom section)
12. Security headers page (`/security-headers`) and/or DevTools Network tab showing response headers
13. Burp Suite: captured traditional `POST /api/auth/login` request/response
14. Burp Suite: captured OAuth-related request (authorize redirect or callback)
15. OWASP ZAP scan results / alerts panel
16. Nmap scan output of `127.0.0.1` showing the four expected ports
17. Vulnerability assessment table (from your completed `vulnerability-assessment.md`, rendered or exported)
18. Audit log page (`/audit`) showing both PASSWORD and OAUTH events
19. Swagger UI (`http://localhost:8080/swagger-ui.html`)
20. Terminal output of `docker compose ps` / `docker ps` showing all 4 containers running
