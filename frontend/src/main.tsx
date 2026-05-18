import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { CssBaseline } from "@mui/material";
import { AppProviders } from "shared-components";
import { App } from "./App";

/** Entry point. Mounts the React app under #root with shared providers. */
const rawPortailApiOrigin = import.meta.env.VITE_PORTAIL_API_ORIGIN as string | undefined;
const isLocalPortail = rawPortailApiOrigin?.startsWith("http://localhost:") ?? false;
const PORTAIL_API_ORIGIN = import.meta.env.DEV && isLocalPortail
  ? "/portail-api"
  : rawPortailApiOrigin;

const container = document.getElementById("root");
if (!container) throw new Error("Missing #root element in index.html");
createRoot(container).render(
  <StrictMode>
    <AppProviders
      userId="default"
      defaultLanguage="fr"
      strictMode={false}
      portailApiOrigin={PORTAIL_API_ORIGIN}
    >
      <CssBaseline />
      <App />
    </AppProviders>
  </StrictMode>
);
