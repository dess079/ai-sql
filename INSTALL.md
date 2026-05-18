# AI-SQL — Installation Guide

## Prerequisites

| Tool | Version |
|------|---------|
| Java (Temurin) | 25 |
| Maven | 3.9+ |
| Docker | 24+ |
| PostgreSQL container | running on port `5460` |
| OpenAI API key | required at runtime |

---

## 1. PostgreSQL container

The demo uses the `cabinet_dentaire_postgres` Docker container on port `5460`.
Start it if not already running:

```bash
docker start cabinet_dentaire_postgres
```

---

## 2. Create the database

Run once:

```bash
docker exec cabinet_dentaire_postgres psql -U postgres \
  -c "CREATE USER ai_sql WITH PASSWORD 'ai_sql';"

docker exec cabinet_dentaire_postgres psql -U postgres \
  -c "CREATE DATABASE ai_sql_demo OWNER ai_sql;"
```

Then apply the migrations in order:

```bash
psql postgresql://ai_sql:ai_sql@localhost:5462/ai_sql_demo \
  -f backend/src/main/resources/db/migration/V1__ai_sql_schema.sql

psql postgresql://ai_sql:ai_sql@localhost:5462/ai_sql_demo \
  -f backend/src/main/resources/db/migration/V2__add_mes_query_result.sql
```

| Script | Description |
|--------|-------------|
| `V1__ai_sql_schema.sql` | Schéma initial — tables `tb_con_conversations` et `tb_mes_messages` |
| `V2__add_mes_query_result.sql` | Ajout de `mes_query_result TEXT` — stockage JSON des résultats (charts, mermaid…) |

> **Note:** Les colonnes ID sont de type `TEXT` pour correspondre au mapping JPA `String`.
> Ne pas utiliser le type natif `uuid` de PostgreSQL.

---

## 3. Build the starter

```bash
cd /Users/sd/dev/libs/ai-sql-spring-boot-starter
mvn -DskipTests install
```

This installs `dev.sd:ai-sql-spring-boot-starter:0.1.0` to `~/.m2`.

---

## 4. Build the demo app

```bash
cd /Users/sd/dev/ai-sql/backend
mvn -DskipTests package
```

---

## 5. Run the demo

```bash
SPRING_PROFILES_ACTIVE=local \
OPENAI_API_KEY=sk-... \
java --enable-preview -jar backend/target/ai-sql-demo-0.1.0.jar
```

API available at **http://localhost:4000**
Health check: **http://localhost:4000/actuator/health**

---

## Configuration

Key properties in `backend/src/main/resources/application.yml`:

| Property | Default | Description |
|----------|---------|-------------|
| `OPENAI_API_KEY` | *(required)* | OpenAI API key |
| `AI_SQL_DEFAULT_MODEL` | `gpt-4o-mini` | Chat model |
| `ai-sql.sql.allow-mutations` | `false` | Allow INSERT/UPDATE/DELETE |
| `ai-sql.sql.max-rows` | `1000` | Max rows returned |
| `ai-sql.schema.default-schema` | `public` | Schema to introspect |
