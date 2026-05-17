CREATE TABLE reservas (
                          id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                          usuario_id    BIGINT       NOT NULL,
                          cancha_id     BIGINT       NOT NULL,
                          horario_id    BIGINT       NOT NULL,
                          fecha_reserva TIMESTAMP    NOT NULL,
                          estado        VARCHAR(50)  NOT NULL
);