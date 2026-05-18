-- V2 — Ajout de la colonne mes_query_result
-- Stocke le résultat de requête sérialisé en JSON (graphique, mermaid, table…)
-- afin de pouvoir le ré-afficher dans l'historique de session.

ALTER TABLE ai_sql.tb_mes_messages
    ADD COLUMN IF NOT EXISTS mes_query_result TEXT;
