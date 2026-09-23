import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

/**
 * Vite config for the ai-sql demo frontend.
 * Profiles map by env var AI_SQL_PROFILE: local=5001, container=5002, cloud=5003.
 */
const PROFILE_PORTS: Record<string, number> = { local: 5001, container: 5002, cloud: 5003 };
const profile = process.env.AI_SQL_PROFILE ?? "local";

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), "");
  const portailTarget = env.VITE_PORTAIL_API_ORIGIN || "http://localhost:8080";

  // Dans le conteneur Docker, l'arborescence des libs `file:` est figée à
  // /app/libs/... ; les symlinks relatifs du node_modules du frontend
  // pointent ailleurs. On force alors la résolution absolue.
  const inContainer = process.env.AI_SQL_CONTAINER_BUILD === "1";

  return {
    plugins: [react()],
    resolve: {
      dedupe: ["react", "react-dom", "@emotion/react", "@emotion/styled"],
      alias: inContainer
        ? {
            "shared-components": "/app/libs/shared-components/dist/index.esm.js",
            "@sd/ai-sql": "/app/libs/ai-sql-lib/dist/index.esm.js",
            "@sd/ai-sql/ui": "/app/libs/ai-sql-lib/dist/ui/index.esm.js",
            "@sd/ai-sql/hooks": "/app/libs/ai-sql-lib/dist/hooks/index.esm.js",
          }
        : {},
    },
    // Les libs `file:` ne ré-exportent pas MUI/emotion/react (externes rollup) :
    // on les externalise pour Vite comme le fait le build hôte via node_modules.
    build: {
      rollupOptions: {
        external: [/^@mui\//, /^@emotion\//, /^@auth0\//],
      },
    },
    server: {
      port: PROFILE_PORTS[profile] ?? 5001,
      host: true,
      strictPort: true,
      fs: { allow: ["../..", "../../libs/ai-sql-lib"] },
      proxy: {
        "/portail-api": {
          target: portailTarget,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/portail-api/, ""),
        },
      },
    },
    preview: { port: PROFILE_PORTS[profile] ?? 5001 },
  };
});
