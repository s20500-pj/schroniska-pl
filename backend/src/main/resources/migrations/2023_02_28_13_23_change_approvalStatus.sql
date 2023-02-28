--liquibase formatted sql
--changeset s20157:2023_02_28_13_23_change_approvalStatus.sql

ALTER TABLE IF EXISTS users
    ADD COLUMN IF NOT EXISTS approval_status VARCHAR(30);

UPDATE users
SET approval_status = 'CONFIRMED'
WHERE id = 2; /*shelter@shelter*/
UPDATE users
SET approval_status = 'CONFIRMED'
WHERE id = 3; /*shelter2@shelter*/

