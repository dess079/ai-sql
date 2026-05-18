# TODO — ai-sql

## Implementation phases

- [x] Phase 1: Scaffold workspace + 2 libs + app
- [ ] Phase 2: TypeScript types (`@sd/ai-sql`)
- [ ] Phase 3: Spring AI provider layer (starter)
- [ ] Phase 4: Schema extraction + formatter
- [ ] Phase 5: Backend services (orchestrator, SSE, validator)
- [ ] Phase 6: Frontend hooks
- [ ] Phase 7: Frontend components
- [ ] Phase 8: App integration (backend + frontend wiring)
- [ ] Phase 9: Rendering pipeline (markdown, mermaid, recharts)
- [ ] Phase 10: Session resume/replay banner
- [ ] Phase 11: Tests + verification

## Manual smoke tests

1. `mvn spring-boot:run -Plocal` in `backend/` → http://localhost:4000/api/ai-sql/health = 200
2. `pnpm dev` in `frontend/` → http://localhost:5000 loads chat UI
3. Ask "list the 5 most recent orders" → see all SSE events stream in timeline
4. Click model chip → switch from `gpt-4o` to `claude-opus-4-5` → token bar updates
5. Ask "draw an ERD of the schema" → Mermaid diagram renders
6. Ask "show monthly revenue as a bar chart" → recharts graph renders
7. Send query → switch browser tab → return → "Session restored" snackbar
8. Refresh browser → session restored from `?session=` URL
9. Create new conversation → previous one stays in sidebar
10. Try malicious "DROP TABLE users" → blocked by SqlValidator, error event shown
