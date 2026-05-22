ALTER TABLE precios
    ADD COLUMN moneda VARCHAR(10);

ALTER TABLE precios
    ADD COLUMN estado VARCHAR(50);

ALTER TABLE precios
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE precios
    ADD COLUMN updated_at TIMESTAMP;

UPDATE precios
SET moneda = 'CLP',
    estado = 'ACTIVO',
    created_at = CURRENT_TIMESTAMP,
    updated_at = CURRENT_TIMESTAMP
WHERE moneda IS NULL;

ALTER TABLE precios
    ALTER COLUMN moneda SET NOT NULL;

ALTER TABLE precios
    ALTER COLUMN estado SET NOT NULL;

ALTER TABLE precios
    ALTER COLUMN created_at SET NOT NULL;