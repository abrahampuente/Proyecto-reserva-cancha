CREATE TABLE resenas (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         usuario_id BIGINT NOT NULL,
                         cancha_id BIGINT NOT NULL,
                         comentario VARCHAR(500) NOT NULL,
                         calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
                         fecha_creacion TIMESTAMP
);