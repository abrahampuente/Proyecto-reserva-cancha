ALTER TABLE notificaciones
    ADD COLUMN estado VARCHAR(50);

ALTER TABLE notificaciones
    ADD COLUMN fecha_actualizacion TIMESTAMP NULL;

UPDATE notificaciones
SET estado = 'ENVIADA',
    fecha_actualizacion = CURRENT_TIMESTAMP
WHERE estado IS NULL;

ALTER TABLE notificaciones
    MODIFY COLUMN estado VARCHAR(50) NOT NULL;
