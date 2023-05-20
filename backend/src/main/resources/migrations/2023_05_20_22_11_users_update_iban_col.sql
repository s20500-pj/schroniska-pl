--liquibase formatted sql
--changeset s20157:2023_05_20_22_11_users_update_iban_col.sql

ALTER TABLE IF EXISTS users
    ALTER COLUMN iban TYPE VARCHAR (100);
