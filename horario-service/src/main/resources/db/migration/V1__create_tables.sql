CREATE TABLE horarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          cancha_id BIGINT,
                          day_of_week VARCHAR(50),
                          start_time TIME,
                          end_time TIME,
                          available BOOLEAN,
                          status VARCHAR(50),
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP
);