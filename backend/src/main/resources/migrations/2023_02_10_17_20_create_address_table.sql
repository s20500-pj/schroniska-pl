--liquibase formatted sql
--changeset s20157:2023_02_10_17_20_create_address_table.sql

CREATE TABLE IF NOT EXISTS address
(
    id              SERIAL       NOT NULL PRIMARY KEY,
    street          VARCHAR(100) NOT NULL,
    city            VARCHAR(60)  NOT NULL,
    postal_code     VARCHAR(30)  NOT NULL,
    building_number VARCHAR(15)  NOT NULL,
    flat_number     VARCHAR(15),
    phone           VARCHAR(15)  NOT NULL,
    KRS_number      VARCHAR(10)
);

ALTER TABLE IF EXISTS users
    ADD COLUMN IF NOT EXISTS address_id INTEGER,
    ADD FOREIGN KEY (address_id) REFERENCES address (id);

INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number)
VALUES ('sezamkowa', 'Gdańsk', '80331', '5', NULL, '555602333', '1234567890'),
       ('sportowa', 'Bydgoszcz', '85252', '25', '5', '605666321', '0987654321'),
       ('kujawska', 'Warszawa', '00805', '1', '3', '546881246', NULL),
       ('kundziasta', 'Lublin', '20321', '12', '3', '123478554', NULL);

UPDATE users
SET address_id = 1
WHERE id = 2; /*shelter@shelter*/
UPDATE users
SET address_id = 2
WHERE id = 3; /*shelter2@shelter*/
UPDATE users
SET address_id = 3
WHERE id = 5; /*user@user*/
UPDATE users
SET address_id = 4
WHERE id = 6; /*user2@user*/

