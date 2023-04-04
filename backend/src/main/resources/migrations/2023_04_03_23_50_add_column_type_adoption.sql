--liquibase formatted sql
--changeset s20157:2023_04_03_23_50_add_column_type_adoption.sql

ALTER TABLE IF EXISTS adoptions
    ADD COLUMN IF NOT EXISTS valid_until DATE;
