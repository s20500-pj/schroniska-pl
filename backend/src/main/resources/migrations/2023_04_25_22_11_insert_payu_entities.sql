--liquibase formatted sql
--changeset s20157:2023_04_25_22_11_insert_payu_entities.sql

INSERT INTO payuclientCredentials(clientId, clientSecret, merchantPosId, shelter_id)
VALUES ('463117', '913d4d4fc69fdadceb45f3469fc89341', '463117', 2),
       ('464332', '90f4f19621b3ca839378add644486b0f', '464332', 3);
