--liquibase formatted sql
--changeset s20157:2023_02_24_13_23_add_column_approvalStatus

ALTER TABLE IF EXISTS users
    ADD COLUMN IF NOT EXISTS approval_status VARCHAR(30);

UPDATE users
SET approval_status = 'POTWIERDZONY'
WHERE id = 2; /*shelter@shelter*/
UPDATE users
SET approval_status = 'POTWIERDZONY'
WHERE id = 3; /*shelter2@shelter*/

