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

  return {
    plugins: [react()],
    resolve: {
      dedupe: ["react", "react-dom", "@emotion/react", "@emotion/styled"],
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
