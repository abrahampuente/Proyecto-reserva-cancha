CREATE TABLE mantenimientos (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                cancha_id BIGINT NOT NULL,
                                descripcion VARCHAR(500) NOT NULL,
                                fecha_inicio DATE NOT NULL,
                                fecha_fin DATE,
                                estado VARCHAR(50) NOT NULL,
                                tecnico VARCHAR(255) NOT NULL,
                                fecha_creacion TIMESTAMP
);