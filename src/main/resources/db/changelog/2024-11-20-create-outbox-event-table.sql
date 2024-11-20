--liquibase formatter sql

--changeset Nika Avalishvili:1
CREATE TABLE outbox_event(
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(255) NOT NULL,
    payload_id BIGINT UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_payload
        FOREIGN KEY(payload_id)
            REFERENCES utility_bill(id)
            ON DELETE SET NULL
);