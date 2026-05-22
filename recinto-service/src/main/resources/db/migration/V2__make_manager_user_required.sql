UPDATE recintos
SET manager_user_id = 1
WHERE manager_user_id IS NULL;

ALTER TABLE recintos
    ALTER COLUMN manager_user_id SET NOT NULL;

UPDATE recintos
SET status = 'ACTIVO'
WHERE status IS NULL;

ALTER TABLE recintos
    ALTER COLUMN status SET NOT NULL;