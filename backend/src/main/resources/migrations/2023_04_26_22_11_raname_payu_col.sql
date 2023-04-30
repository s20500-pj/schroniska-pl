--liquibase formatted sql
--changeset s20157:2023_04_25_22_11_users_add_iban_col

ALTER TABLE payuclientcredentials RENAME COLUMN clientId TO client_id;
ALTER TABLE payuclientcredentials RENAME COLUMN clientSecret TO client_secret;
ALTER TABLE payuclientcredentials RENAME COLUMN merchantPosId TO merchant_pos_id;
