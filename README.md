# AuthCompare Security Lab

OAuth 2.0 Authorization Code Flow vs traditional username/password authentication.
This project is scoped to localhost for an authorized college security lab.

## Included services

- React frontend: http://localhost:3000
- Spring Boot API + Swagger: http://localhost:8080/swagger-ui.html
- Local Spring Authorization Server: http://localhost:9000
- MySQL 8 database (authlab) on localhost:3306
- BCrypt, JWT cookie/Bearer auth, USER/ADMIN authorization
- Real Authorization Code + OIDC flow with state, PKCE, exact redirect URI matching,
  short-lived single-use codes, short-lived tokens, and refresh-token rotation

## MySQL Workbench connection

MySQL Workbench is only the graphical client. MySQL Server 8 must also be installed
and running.

| Field | Value |
| --- | --- |
| Hostname | localhost |
| Port | 3306 |
| Username | root |
| Password | hari |

Open database/schema/init.sql in Workbench and execute all statements. The backend
also uses createDatabaseIfNotExist=true and Hibernate ddl-auto=update.

## Start in VS Code on Windows

Open the project root in VS Code. Ensure MySQL Server is running, then use three
PowerShell terminals.

Terminal 1:

~~~powershell
cd oauth-server
mvn spring-boot:run
~~~

Terminal 2:

~~~powershell
cd backend
mvn spring-boot:run
~~~

Terminal 3:

~~~powershell
cd frontend
npm install
npm start
~~~

Defaults already match root / hari. To override them:

~~~powershell
$env:DATABASE_HOST="localhost"
$env:DATABASE_PORT="3306"
$env:DATABASE_NAME="authlab"
$env:DATABASE_USERNAME="root"
$env:DATABASE_PASSWORD="hari"
mvn spring-boot:run
~~~

## Swagger test order

1. Open http://localhost:8080/swagger-ui.html.
2. Run POST /api/auth/register:

~~~json
{
  "username": "hari",
  "email": "hari@example.com",
  "password": "Password123!"
}
~~~

3. Run POST /api/auth/login with the same username/password.
4. Copy only accessToken from the response.
5. Click Authorize, paste the token, and confirm.
6. Test GET /api/auth/me and GET /api/audit/events.
7. GET /api/admin/users correctly returns 403 until you promote the account:

~~~sql
USE authlab;
UPDATE users SET role = 'ADMIN' WHERE username = 'hari';
~~~

8. Log in again to get a new ADMIN JWT, re-authorize, and retry the admin API.

Swagger and the register/login entry points are public. Protected APIs remain
protected so the lab demonstrates authorization rather than making every API public.

## OAuth test

Start all services, visit http://localhost:3000/oauth/login, and use:

- Username: labuser
- Password: LabPassword123!

Approve consent. The backend exchanges the authorization code using PKCE, creates
the local session, and redirects to the dashboard. The client app never receives
the OAuth provider password.

## Docker alternative

Stop locally installed MySQL first if it occupies port 3306.

~~~powershell
Copy-Item .env.example .env
cd docker
docker compose --env-file ../.env up --build
~~~

Docker binds MySQL to 127.0.0.1 so Workbench can connect without exposing it to
the LAN.

## Verification commands

~~~powershell
cd backend
mvn clean test

cd ../oauth-server
mvn clean test

cd ../frontend
npm install
npm run build
~~~

## Completion status

- Backend entities/configuration: complete
- MySQL schema and Workbench defaults: complete
- Traditional auth and Swagger Bearer testing: complete
- Local OAuth/OIDC + PKCE: complete
- React production build: passed
- Docker configuration: complete
- Integration tests needing your running MySQL Server: still to run locally
- Burp, ZAP, Nmap evidence/screenshots: must be captured locally; never fabricate

Do not reuse these lab passwords or default secrets in a public deployment.
