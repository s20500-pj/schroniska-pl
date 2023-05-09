--liquibase formatted sql
--changeset s20500:2023_05_07_23_30_update_animals_status.sql

UPDATE animals
SET animal_status = (CASE
                         WHEN animal_status = '0' THEN 'UNKNOWN'
                         WHEN animal_status = '1' THEN 'NEEDS_MEDICAL_TREATMENT'
                         WHEN animal_status = '2' THEN 'READY_FOR_ADOPTION'
                         WHEN animal_status = '3' THEN 'ADOPTED'
                         WHEN animal_status = '4' THEN 'DEAD'
                         WHEN animal_status = '5' THEN 'DELETED'
                         WHEN animal_status = '6' THEN 'VIRTUAL_ADOPTED'
    END)
WHERE animal_status IN ('0', '1', '2', '3', '4', '5', '6');