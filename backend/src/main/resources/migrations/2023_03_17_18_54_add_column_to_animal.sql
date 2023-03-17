--liquibase formatted sql
--changeset s20500:2023_03_17_18_54_add_column_to_animal.sql

ALTER TABLE IF EXISTS animals
    ADD COLUMN IF NOT EXISTS available_for_walk BOOLEAN;