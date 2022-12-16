--liquibase formatted sql
--changeset s20500:08_12_2022_23_06_create_user_table.sql

CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);