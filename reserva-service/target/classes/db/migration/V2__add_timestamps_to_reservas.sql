ALTER TABLE reservas
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE reservas
    ADD COLUMN updated_at TIMESTAMP;

UPDATE reservas
SET created_at = CURRENT_TIMESTAMP,
    updated_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;