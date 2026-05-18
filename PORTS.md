# PORTS.md — Source of truth for `ai-sql`

Never guess a port. Always consult this file.

## Spring profiles

| Profile     | Backend (Spring Boot) | Frontend (Vite / nginx) |
|-------------|-----------------------|--------------------------|
| `local`     | **4000**              | **5001**                 |
| `container` | **4001**              | **5002**                 |
| `cloud`     | **4002**              | **5002**                 |

## Convention

- `local` = developer workstation, both apps run directly on host
- `container` = Docker Compose stack, ports exposed +1
- `cloud` = deployed environment, ports exposed +2

## Files of record

- Backend: `backend/src/main/resources/application.yml` (`spring.profiles.*` blocks)
- Frontend: `frontend/vite.config.ts` (`server.port`) and container `nginx.conf`

## PostgreSQL

| Profile     | Host port | Container port | Docker container      |
|-------------|-----------|----------------|-----------------------|
| `local`     | 5462      | n/a            | ai-sql-postgres       |
| `container` | 5462      | 5432           | ai-sql-postgres       |
| `cloud`     | provider-managed | n/a       | n/a                   |

PostgreSQL image: `postgres:18.3`

## pgAdmin

| Service        | Host port | URL                        |
|----------------|-----------|----------------------------|
| ai-sql-pgadmin | **5051**  | http://localhost:5051       |

Credentials: `admin@aisql.dev` / `admin`
Servers pré-configurés: `ai-sql` (5462) + `cabinet-dentaire` (5460)
