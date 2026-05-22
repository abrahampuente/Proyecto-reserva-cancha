ALTER TABLE pagos
    ADD COLUMN moneda VARCHAR(10);

ALTER TABLE pagos
    ADD COLUMN codigo_transaccion VARCHAR(100);

ALTER TABLE pagos
    ADD COLUMN fecha_pago TIMESTAMP;

ALTER TABLE pagos
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE pagos
    ADD COLUMN updated_at TIMESTAMP;

UPDATE pagos
SET moneda = 'CLP',
    fecha_pago = CURRENT_TIMESTAMP,
    created_at = CURRENT_TIMESTAMP,
    updated_at = CURRENT_TIMESTAMP
WHERE moneda IS NULL;

ALTER TABLE pagos
    ALTER COLUMN moneda SET NOT NULL;

ALTER TABLE pagos
    ALTER COLUMN fecha_pago SET NOT NULL;

ALTER TABLE pagos
    ALTER COLUMN created_at SET NOT NULL;