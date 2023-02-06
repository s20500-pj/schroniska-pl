--liquibase formatted sql
--changeset s20500:2023_02_05_17_20_add_role_user_roles_table.sql

CREATE TABLE IF NOT EXISTS roles
(
    id   SERIAL       NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE IF EXISTS users
    ADD COLUMN IF NOT EXISTS email        VARCHAR(255),
    ADD COLUMN IF NOT EXISTS password     VARCHAR(255),
    ADD COLUMN IF NOT EXISTS shelter_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS is_disabled  BOOLEAN;

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO roles(name)
VALUES ('ROLE_USER'),
       ('ROLE_SHELTER'),
       ('ROLE_MODERATOR'),
       ('ROLE_ADMIN');

INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled)
VALUES ('Marian', 'Kowalski', 'admin@admin', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL,
        FALSE),
       (NULL, NULL, 'shelter@shelter', '$2a$10$RJT94MguFJUrAFJDGQkg8e6.nQSzRLBJczJ0duJDdUsY5qBGWr.3a', 'Oaza',
        FALSE),
       (NULL, NULL, 'shelter2@shelter', '$2a$10$FYP5twQneHvU/z2h.VNWo.4h53lBYqW/Ydl2kodHcdlU9Bfa5O7c6', 'Kociapka',
        FALSE),
       ('Zdzisław', 'Wiertara', 'mod@mod', '$2a$10$6902uICXuFG.P5AUJvIYSuINVlt/T2445PrBRW6DCZXODRLFKNc4a', NULL,
        FALSE),
       ('Lolita', 'Kowalska', 'user@user', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL,
        FALSE),
       ('Henryk', 'Korcipa', 'user2@user', '$2a$10$a9Iy/BZmvmvbcTCCAsfun.GS7AIWQgHDZVyCzfNMQNFt7BE/BfPC6', NULL,
        FALSE);

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 4),
       (2, 2),
       (3, 2),
       (4, 3),
       (5, 1),
       (6, 1);