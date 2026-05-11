CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone VARCHAR(20) NOT NULL,
                       role VARCHAR(100),
                       status VARCHAR(100),
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP
);

CREATE TABLE user_profiles (
                               id BIGINT PRIMARY KEY,
                               address VARCHAR(255),
                               city VARCHAR(255),
                               commune VARCHAR(255),
                               CONSTRAINT fk_user_profile_user
                                   FOREIGN KEY (id)
                                       REFERENCES users(id)
);