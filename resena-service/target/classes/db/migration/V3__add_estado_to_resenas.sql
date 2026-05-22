ALTER TABLE resenas
    ADD COLUMN estado VARCHAR(50);

UPDATE resenas
SET estado = 'ACTIVA'
WHERE estado IS NULL;

ALTER TABLE resenas
    ALTER COLUMN estado SET NOT NULL;