--liquibase formatted sql
--changeset s20500:2023_25_02_23_50_add_column_type.sql

ALTER TABLE IF EXISTS users
    ADD COLUMN IF NOT EXISTS user_type VARCHAR(30);

UPDATE users
SET user_type = 'SHELTER'
WHERE shelter_name IS NOT NULL;
UPDATE users
SET user_type = 'PERSON'
WHERE first_name IS NOT NULL;