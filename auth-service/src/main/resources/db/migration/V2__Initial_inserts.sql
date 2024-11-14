-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

USE psira_auth_database;

INSERT INTO user (username, password, email, role) VALUES
    ('admin_user', 'SecureAdminPassword1', 'admin@example.com', 'ADMIN'),
    ('applicant_user1', 'SecureApplicantPassword1', 'applicant1@example.com', 'APPLICANT'),
    ('applicant_user2', 'SecureApplicantPassword2', 'applicant2@example.com', 'APPLICANT');

INSERT INTO sessions (user_id, token, expires_at) VALUES
    (1, 'token1234567890', '2024-12-31 23:59:59'),
    (2, 'token0987654321', '2024-12-31 23:59:59'),
    (3, 'token1122334455', '2024-12-31 23:59:59');
