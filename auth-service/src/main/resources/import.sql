-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

CREATE SCHEMA IF NOT EXISTS psirs_auth_database;
USE psirs_auth_database;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role ENUM('admin', 'applicant') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- Insert users
INSERT INTO users (username, password, email) VALUES
  ('john_doe', 'hashed_password_1', 'john.doe@example.com'),
  ('jane_smith', 'hashed_password_2', 'jane.smith@example.com'),
  ('alice_johnson', 'hashed_password_3', 'alice.johnson@example.com');

-- Insert sessions
INSERT INTO sessions (user_id, token, expires_at) VALUES
  (1, 'token_for_john_doe', DATE_ADD(NOW(), INTERVAL 1 HOUR)),
  (2, 'token_for_jane_smith', DATE_ADD(NOW(), INTERVAL 2 HOUR)),
  (3, 'token_for_alice_johnson', DATE_ADD(NOW(), INTERVAL 30 MINUTE));

