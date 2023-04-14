--liquibase formatted sql
--changeset s20157:2023_04_14_09_37_add_payuentity.sql.sql

CREATE TABLE IF NOT EXISTS payuclientCredentials
(
    id            SERIAL PRIMARY KEY,
    clientId      VARCHAR(255),
    clientSecret  VARCHAR(255),
    merchantPosId VARCHAR(255),
    shelter_id    BIGINT NOT NULL,
    FOREIGN KEY (shelter_id) REFERENCES users (id)
);
