--liquibase formatted sql
--changeset s20500:2023_03_12_18_52_add_animal_adoption.sql

ALTER TABLE IF EXISTS users
    ADD COLUMN IF NOT EXISTS information TEXT;

CREATE TABLE IF NOT EXISTS animals
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    information      TEXT,
    species          VARCHAR(255),
    sex              VARCHAR(255),
    age              VARCHAR(255),
    birth_date       DATE,
    animal_status    VARCHAR(255),
    sterilized       BOOLEAN,
    vaccinated       BOOLEAN,
    kids_friendly    BOOLEAN,
    couch_potato     BOOLEAN,
    needs_activeness BOOLEAN,
    cats_friendly    BOOLEAN,
    dogs_friendly    BOOLEAN,
    user_id          BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS adoptions
(
    id              SERIAL PRIMARY KEY,
    adoption_type   VARCHAR(20),
    adoption_status VARCHAR(20),
    user_id         BIGINT NOT NULL,
    animal_id       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (animal_id) REFERENCES animals (id)
);
