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

INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled, user_type)
VALUES ('Marian', 'Kowalski', 'admin@admin.pl', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL,
        FALSE, 'ADMIN'),
       (NULL, NULL, 'shelter@shelter.pl', '$2a$10$RJT94MguFJUrAFJDGQkg8e6.nQSzRLBJczJ0duJDdUsY5qBGWr.3a', 'Oaza',
        FALSE, 'SHELTER'),
       (NULL, NULL, 'shelter2@shelter.pl', '$2a$10$FYP5twQneHvU/z2h.VNWo.4h53lBYqW/Ydl2kodHcdlU9Bfa5O7c6', 'Kociapka',
        FALSE, 'SHELTER'),
       ('Zdzisław', 'Wiertara', 'mod@mod.pl', '$2a$10$6902uICXuFG.P5AUJvIYSuINVlt/T2445PrBRW6DCZXODRLFKNc4a', NULL,
        FALSE, 'PERSON'),
       ('Lolita', 'Kowalska', 'user@user.pl', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL,
        FALSE, 'PERSON'),
       ('Henryk', 'Korcipa', 'user2@user.pl', '$2a$10$a9Iy/BZmvmvbcTCCAsfun.GS7AIWQgHDZVyCzfNMQNFt7BE/BfPC6', NULL,
        FALSE, 'PERSON');

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 4),
       (2, 2),
       (3, 2),
       (4, 3),
       (5, 1),
       (6, 1);