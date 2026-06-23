ALTER TABLE precios
    ADD COLUMN moneda VARCHAR(10);

ALTER TABLE precios
    ADD COLUMN estado VARCHAR(50);

ALTER TABLE precios
    ADD COLUMN created_at TIMESTAMP NULL;

ALTER TABLE precios
    ADD COLUMN updated_at TIMESTAMP NULL;

UPDATE precios
SET moneda = 'CLP',
    estado = 'ACTIVO',
    created_at = CURRENT_TIMESTAMP,
    updated_at = CURRENT_TIMESTAMP
WHERE moneda IS NULL;

ALTER TABLE precios
    MODIFY COLUMN moneda VARCHAR(10) NOT NULL;

ALTER TABLE precios
    MODIFY COLUMN estado VARCHAR(50) NOT NULL;

ALTER TABLE precios
    MODIFY COLUMN created_at TIMESTAMP NOT NULL;
