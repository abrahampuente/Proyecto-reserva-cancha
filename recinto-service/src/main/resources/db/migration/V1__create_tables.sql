CREATE TABLE recintos (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(255) NOT NULL,
                          city VARCHAR(255) NOT NULL,
                          commune VARCHAR(255) NOT NULL,
                          phone VARCHAR(255) NOT NULL,
                          manager_user_id BIGINT NULL,
                          status VARCHAR(100) NULL,
                          created_at DATETIME(6) NULL,
                          updated_at DATETIME(6) NULL,
                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE imagenes_recinto (
                                  id BIGINT NOT NULL AUTO_INCREMENT,
                                  image_url VARCHAR(255) NULL,
                                  description VARCHAR(255) NULL,
                                  recinto_id BIGINT NULL,
                                  PRIMARY KEY (id),
                                  CONSTRAINT fk_imagenes_recinto_recinto
                                      FOREIGN KEY (recinto_id)
                                          REFERENCES recintos (id)
                                          ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;