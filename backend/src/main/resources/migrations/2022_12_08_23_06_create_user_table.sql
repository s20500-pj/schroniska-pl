--liquibase formatted sql
--changeset s20500:2022_12_08_23_06_create_user_table.sql

CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    user_type  VARCHAR(30)
);