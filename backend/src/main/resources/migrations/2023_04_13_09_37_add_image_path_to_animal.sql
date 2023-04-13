--liquibase formatted sql
--changeset s20500:2023_04_13_09_37_add_image_path_to_animal.sql

ALTER TABLE IF EXISTS animals
    ADD COLUMN IF NOT EXISTS image_path TEXT;

ALTER TABLE IF EXISTS adoptions
    DROP COLUMN image_path;
