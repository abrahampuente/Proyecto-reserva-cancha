ALTER TABLE mantenimientos
    ADD COLUMN fecha_actualizacion TIMESTAMP;

UPDATE mantenimientos
SET fecha_actualizacion = CURRENT_TIMESTAMP
WHERE fecha_actualizacion IS NULL;