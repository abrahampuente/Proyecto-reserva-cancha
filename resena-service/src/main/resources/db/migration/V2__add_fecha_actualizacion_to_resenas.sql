ALTER TABLE resenas
    ADD COLUMN fecha_actualizacion TIMESTAMP;

UPDATE resenas
SET fecha_actualizacion = CURRENT_TIMESTAMP
WHERE fecha_actualizacion IS NULL;