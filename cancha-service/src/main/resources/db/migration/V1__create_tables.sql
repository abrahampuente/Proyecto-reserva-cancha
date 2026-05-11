CREATE TABLE canchas (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         sport_type VARCHAR(255) NOT NULL,
                         surface_type VARCHAR(255) NOT NULL,
                         capacity INT NOT NULL,
                         recinto_id BIGINT NOT NULL,
                         status VARCHAR(100),
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP
);

CREATE TABLE caracteristicas_cancha (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255),
                                        cancha_id BIGINT,
                                        CONSTRAINT fk_cancha
                                            FOREIGN KEY (cancha_id)
                                                REFERENCES canchas(id)
);