--liquibase formatted sql
--changeset s20157:2023_04_25_22_11_users_add_iban_col

ALTER TABLE IF EXISTS users
    ADD COLUMN IF NOT EXISTS iban VARCHAR(40);
