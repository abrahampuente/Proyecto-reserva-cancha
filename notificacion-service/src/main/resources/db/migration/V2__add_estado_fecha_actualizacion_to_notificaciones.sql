ALTER TABLE notificaciones
    ADD COLUMN estado VARCHAR(50);

ALTER TABLE notificaciones
    ADD COLUMN fecha_actualizacion TIMESTAMP;

UPDATE notificaciones
SET estado = 'ENVIADA',
    fecha_actualizacion = CURRENT_TIMESTAMP
WHERE estado IS NULL;

ALTER TABLE notificaciones
    ALTER COLUMN estado SET NOT NULL;