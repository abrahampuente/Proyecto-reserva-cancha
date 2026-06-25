UPDATE recintos
SET manager_user_id = 1
WHERE manager_user_id IS NULL;

ALTER TABLE recintos
    MODIFY COLUMN manager_user_id BIGINT NOT NULL;

UPDATE recintos
SET status = 'ACTIVO'
WHERE status IS NULL;

ALTER TABLE recintos
    MODIFY COLUMN status VARCHAR(100) NOT NULL;
