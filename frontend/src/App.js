import { jsx as _jsx } from "react/jsx-runtime";
import { AISQLChat } from "@sd/ai-sql/ui";
/** Backend base URL — overridable via Vite env (`VITE_BACKEND_URL`). */
const BACKEND_URL = import.meta.env.VITE_BACKEND_URL ?? "http://localhost:4000";
/**
 * Root component. The demo simply renders the drop-in `<AISQLChat>` widget.
 *
 * @returns the demo app shell
 */
export function App() {
    return _jsx(AISQLChat, { backendUrl: BACKEND_URL, userId: "default" });
}
