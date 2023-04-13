--liquibase formatted sql
--changeset s20500:2023_04_12_21_23_add_file_path_and_admin_type.sql

ALTER TABLE IF EXISTS adoptions
    ADD COLUMN IF NOT EXISTS image_path TEXT;

UPDATE users
SET user_type = 'ADMIN'
WHERE email = 'admin@admin';
