CREATE TABLE notificaciones (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                titulo VARCHAR(255) NOT NULL,
                                mensaje VARCHAR(500) NOT NULL,
                                destinatario VARCHAR(255) NOT NULL,
                                tipo VARCHAR(50) NOT NULL,
                                leida BOOLEAN NOT NULL DEFAULT FALSE,
                                fecha_creacion TIMESTAMP
);