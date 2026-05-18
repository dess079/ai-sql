# PLAN — ai-sql

## Goal
Drop-in, generic AI chat that turns natural-language questions into safe SQL on any PostgreSQL database, streams every step over SSE, and renders results as markdown / tables / charts / Mermaid.

## Two libraries + one demo app

| Project | Path | Purpose |
|---|---|---|
| `@sd/ai-sql` (npm) | `libs/ai-sql-lib` | React 19 + MUI v7 UI components, hooks, TS types |
| `ai-sql-spring-boot-starter` (Maven) | `libs/ai-sql-spring-boot-starter` | Spring AI 2.0.0-M4 generic provider layer, SSE orchestrator, schema extractor, conversation store |
| `ai-sql` (demo) | `ai-sql/` | Reference integration: Spring Boot 3.x backend + Vite frontend on PostgreSQL |

## Stack

- Java 21 + Spring Boot 3.3.x + Spring AI **2.0.0-M4**
- React 19 + Vite 6 + TypeScript 5.6 + MUI v7
- PostgreSQL 16 (app data + conversation history, schema `ai_sql`)
- pnpm workspaces, Maven, Rollup

## Spring AI is the generic provider layer

`ChatClient` auto-configured from `application.yml`. Switching providers = config change. Supported: OpenAI, Anthropic, Azure OpenAI, GitHub Models, Ollama, Gemini, Mistral.

## SSE event flow

`session_start → schema_built → prompt_rephrased → step_start* → sql_generated → sql_validated → query_executing → query_result → render_start → content_chunk* → complete | error`

## Schema-to-AI format

Structured text block extracted from `information_schema` + `pg_description`, one column per line with inline PK/FK/UNIQUE/comment annotations. Cached server-side, embedded in system prompt.

## Security

- SELECT-only by default via JSqlParser AST validation
- No DDL/DML to AI-generated SQL
- API keys via env vars only, never logged
- OWASP LLM Top-10 baseline (system prompt isolation, output validation, no `eval`)

## Resume/replay

`sessionId` in URL + localStorage. Server keeps a ring-buffer of events per session. On reconnect → `GET /sessions/{id}/replay` returns all buffered events.

## Verification checklist
See [TODO.md](./TODO.md).
