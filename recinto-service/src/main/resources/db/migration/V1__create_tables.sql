CREATE TABLE recintos (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(255) NOT NULL,
                          city VARCHAR(255) NOT NULL,
                          commune VARCHAR(255) NOT NULL,
                          phone VARCHAR(255) NOT NULL,
                          manager_user_id BIGINT,
                          status VARCHAR(100),
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP
);

CREATE TABLE imagenes_recinto (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  image_url VARCHAR(255),
                                  description VARCHAR(255),
                                  recinto_id BIGINT,
                                  CONSTRAINT fk_recinto
                                      FOREIGN KEY (recinto_id)
                                          REFERENCES recintos(id)
);