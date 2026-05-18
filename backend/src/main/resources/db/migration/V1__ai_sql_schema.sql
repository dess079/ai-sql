-- V1 — Schéma initial ai_sql
-- Tables: conversations + messages

CREATE SCHEMA IF NOT EXISTS ai_sql;

-- ─── Conversations ────────────────────────────────────────────────────────────

CREATE TABLE ai_sql.tb_con_conversations (
    con_id              TEXT                     NOT NULL,
    con_user_id         TEXT                     NOT NULL,
    con_title           TEXT                     NOT NULL,
    con_model           TEXT,
    rec_create_datetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rec_update_datetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_con PRIMARY KEY (con_id)
);

CREATE INDEX idx_con_user_id
    ON ai_sql.tb_con_conversations (con_user_id, rec_update_datetime DESC);

-- ─── Messages ─────────────────────────────────────────────────────────────────

CREATE TABLE ai_sql.tb_mes_messages (
    mes_id              TEXT                     NOT NULL,
    con_id              TEXT                     NOT NULL,
    mes_role            TEXT                     NOT NULL,
    mes_content         TEXT                     NOT NULL,
    mes_sql_text        TEXT,
    mes_model           TEXT,
    rec_create_datetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_mes      PRIMARY KEY (mes_id),
    CONSTRAINT fk_mes_con  FOREIGN KEY (con_id)
        REFERENCES ai_sql.tb_con_conversations (con_id) ON DELETE CASCADE
);

CREATE INDEX idx_mes_con_id
    ON ai_sql.tb_mes_messages (con_id, rec_create_datetime);
