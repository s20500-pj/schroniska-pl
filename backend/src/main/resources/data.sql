INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_SHELTER');
INSERT INTO roles(name) VALUES ('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');

INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Marian', 'Kowalski', 'admin@admin', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL, FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES (NULL, NULL, 'shelter@shelter', '$2a$10$RJT94MguFJUrAFJDGQkg8e6.nQSzRLBJczJ0duJDdUsY5qBGWr.3a', 'Oaza', FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES (NULL, NULL, 'shelter2@shelter', '$2a$10$FYP5twQneHvU/z2h.VNWo.4h53lBYqW/Ydl2kodHcdlU9Bfa5O7c6', 'Kociapka', FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Zdzisław', 'Wiertara', 'mod@mod', '$2a$10$6902uICXuFG.P5AUJvIYSuINVlt/T2445PrBRW6DCZXODRLFKNc4a', NULL, FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Lolita', 'Kowalska', 'user@user', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL, FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Henryk', 'Korcipa', 'user2@user', '$2a$10$a9Iy/BZmvmvbcTCCAsfun.GS7AIWQgHDZVyCzfNMQNFt7BE/BfPC6', NULL, FALSE);

INSERT INTO user_roles(user_id, role_id) VALUES (1, 4);
INSERT INTO user_roles(user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles(user_id, role_id) VALUES (3, 2);
INSERT INTO user_roles(user_id, role_id) VALUES (4, 3);
INSERT INTO user_roles(user_id, role_id) VALUES (5, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (6, 1);

INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('sezamkowa', 'Gdańsk', '80331', '5', NULL, '555602333', '1234567890');
INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('sportowa', 'Bydgoszcz', '85252', '25', '5', '605666321', '0987654321');
INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('kujawska', 'Warszawa', '00805', '1', '3', '546881246', NULL);
INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('kundziasta', 'Lublin', '20321', '12', '3', '123478554', NULL);

UPDATE users SET address_id = 1 WHERE id = 2; /*shelter@shelter*/
UPDATE users SET address_id = 2 WHERE id = 3; /*shelter2@shelter*/
UPDATE users SET address_id = 3 WHERE id = 5; /*user@user*/
UPDATE users SET address_id = 4 WHERE id = 6; /*user2@user*/

UPDATE users SET approval_status = 'CONFIRMED' WHERE id = 2; /*shelter@shelter*/
UPDATE users SET approval_status = 'CONFIRMED' WHERE id = 3; /*shelter2@shelter*/

UPDATE users SET user_type = 'SHELTER' WHERE shelter_name IS NOT NULL;
UPDATE users SET user_type = 'PERSON' WHERE first_name IS NOT NULL;

INSERT INTO payu_client_credentials(client_id, client_secret, merchant_pos_id, shelter_id) VALUES ('463117', '913d4d4fc69fdadceb45f3469fc89341', '463117', 2);

INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Max','Friendly and loves to play fetch','DOG','MALE','ADULT','2019-01-01',2,true,true,true,true,true,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Majster','Friendly and loves to play fetch','DOG','MALE','ADULT','2019-01-01',2,true,true,true,true,true,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('ChinkiChinki','Friendly and loves to play fetch','DOG','MALE','ADULT','2019-01-01',2,true,true,true,true,true,true,true,true,3);

-- select * from users join user_roles on users.id=user_roles.user_id join roles on user_roles.role_id=roles.id;