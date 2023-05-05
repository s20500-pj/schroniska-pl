--liquibase formatted sql
--changeset s20500:2023_05_05_21_18_update_animals.sql

DELETE
FROM animals
WHERE animal_status = '3';