CREATE TABLE IF NOT EXISTS resenas (
                                       id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       usuario_id      BIGINT       NOT NULL,
                                       cancha_id       BIGINT       NOT NULL,
                                       comentario      TEXT         NOT NULL,
                                       calificacion    INT          NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    fecha_creacion  TIMESTAMP
    );