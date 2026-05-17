CREATE TABLE precios (
                         id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                         cancha_id   BIGINT         NOT NULL,
                         valor       DECIMAL(10,2)  NOT NULL,
                         descripcion VARCHAR(255)   NOT NULL
);