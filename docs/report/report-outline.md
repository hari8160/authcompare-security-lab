# Report Outline
### Comparison of OAuth 2.0 Authorization-Code Flow with Traditional Username/Password Authentication in a Web Application

Use this as your section-by-section skeleton. Each section below notes what to pull
from this project.

1. **Objective** — state the comparison goal (from the assignment brief).
2. **Problem Statement** — why comparing these two matters (security trade-offs).
3. **Introduction** — brief overview of both mechanisms.
4. **Brief Theory**
   - Traditional Authentication
   - OAuth 2.0
   - Authorization Code Flow
   - PKCE
   - OpenID Connect
   - Authentication vs Authorization
5. **System Architecture** — use the diagram from the README / `docs/architecture/`.
6. **Technology Stack** — React, Spring Boot, Spring Security, Spring Authorization Server, PostgreSQL, Docker.
7. **Database Design** — `database/schema/init.sql`, ER relationships (users / oauth_accounts / authentication_events).
8. **Implementation** — summarize each backend/frontend module briefly; reference actual file paths.
9. **Authentication Flow** — traditional, step by step (see `/flows` page).
10. **OAuth Flow** — authorization code + PKCE, step by step (see `/flows` page).
11. **Security Controls** — BCrypt, JWT cookie flags, PKCE, state, redirect-uri validation, headers (see `/security-lab`, `SecurityConfig.java`, `AuthorizationServerConfig.java`).
12. **Testing Methodology** — overview of Burp/ZAP/Nmap approach.
13. **Burp Suite Testing** — findings from `docs/testing/testing-guide.md` §1.
14. **OWASP ZAP Testing** — findings from §2.
15. **Nmap Testing** — findings from §3.
16. **Vulnerability Assessment** — populate `vulnerability-assessment.md` with real findings.
17. **Findings** — summarize.
18. **Risk Analysis** — likelihood × impact reasoning per finding.
19. **Remediation** — problem → impact → fix → implementation → retest → result, per finding.
20. **Comparison** — use the `/comparison` page table verbatim or reformatted.
21. **Advantages and Disadvantages** — synthesize from the comparison table.
22. **Secure Coding Practices** — env vars for secrets, DTO validation, parameterized queries, least privilege scopes.
23. **DevSecOps / Secure SDLC** — describe your build → test → scan → deploy sequence; add a GitHub Actions workflow if desired.
24. **Web Security Audit** — populate `web-security-audit-checklist.md` honestly.
25. **Screenshots / Evidence** — see `docs/screenshots/screenshot-checklist.md`.
26. **Conclusion** — restate the core learning outcome (see §31 of the original brief: OAuth doesn't automatically mean "secure" — it depends on correct implementation).
27. **References** — OAuth 2.0 RFC 6749, RFC 7636 (PKCE), OpenID Connect Core spec, OWASP ASVS/Top 10, Spring Security / Spring Authorization Server docs.
