INSERT INTO roles(name) VALUES ('ROLE_USER');
INSERT INTO roles(name) VALUES ('ROLE_SHELTER');
INSERT INTO roles(name) VALUES ('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');

INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Marian', 'Kowalski', 'admin@admin.pl', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL, FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES (NULL, NULL, 'shelter@shelter.pl', '$2a$10$RJT94MguFJUrAFJDGQkg8e6.nQSzRLBJczJ0duJDdUsY5qBGWr.3a', 'Oaza', FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES (NULL, NULL, 'shelter2@shelter.pl', '$2a$10$FYP5twQneHvU/z2h.VNWo.4h53lBYqW/Ydl2kodHcdlU9Bfa5O7c6', 'Kociapka', FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Zdzisław', 'Wiertara', 'mod@mod.pl', '$2a$10$6902uICXuFG.P5AUJvIYSuINVlt/T2445PrBRW6DCZXODRLFKNc4a', NULL, FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Lolita', 'Kowalska', 'user@user.pl', '$2a$10$jaPzsOA4RXYAusxuPUhSbeizQEla2Pxv7arANT9yr.9pGsyuH5c6i', NULL, FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled) VALUES ('Henryk', 'Korcipa', 'user2@user.pl', '$2a$10$a9Iy/BZmvmvbcTCCAsfun.GS7AIWQgHDZVyCzfNMQNFt7BE/BfPC6', NULL, FALSE);
INSERT INTO users(first_name, last_name, email, password, shelter_name, is_disabled, iban) VALUES (NULL, NULL, 'shelter3@shelter.pl', '$2a$10$RJT94MguFJUrAFJDGQkg8e6.nQSzRLBJczJ0duJDdUsY5qBGWr.3a', 'Klopsy', TRUE, '1igc1IkPmsMylP3Y8NctDfckAp4dtVSZuq7NZqBpIv27MWG7iC02gw==');

INSERT INTO user_roles(user_id, role_id) VALUES (1, 4);
INSERT INTO user_roles(user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles(user_id, role_id) VALUES (3, 2);
INSERT INTO user_roles(user_id, role_id) VALUES (4, 3);
INSERT INTO user_roles(user_id, role_id) VALUES (5, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (6, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (7, 2);


INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('sezamkowa', 'Gdańsk', '80331', '5', NULL, '555602333', '1234567890');
INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('sportowa', 'Bydgoszcz', '85252', '25', '5', '605666321', '0987654321');
INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('kujawska', 'Warszawa', '00805', '1', '3', '546881246', NULL);
INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('kundziasta', 'Lublin', '20321', '12', '3', '123478554', NULL);
INSERT INTO address(street, city, postal_code, building_number, flat_number, phone, KRS_number) VALUES ('jakasTam', 'Gdynia', '80441', '54', '5', '563454321', 0983654325);

UPDATE users SET address_id = 1 WHERE id = 2; /*shelter@shelter*/
UPDATE users SET address_id = 2 WHERE id = 3; /*shelter2@shelter*/
UPDATE users SET address_id = 3 WHERE id = 5; /*user@user*/
UPDATE users SET address_id = 4 WHERE id = 6; /*user2@user*/
UPDATE users SET address_id = 5 WHERE id = 7; /*shelter3@shelter*/

UPDATE users SET approval_status = 'COMPLETED' WHERE id = 2; /*shelter@shelter*/
UPDATE users SET approval_status = 'COMPLETED' WHERE id = 3; /*shelter2@shelter*/
UPDATE users SET approval_status = 'EMAIL_NOT_CONFIRMED' WHERE id = 7; /*shelter3@shelter*/

UPDATE users SET user_type = 'SHELTER' WHERE shelter_name IS NOT NULL;
UPDATE users SET user_type = 'PERSON' WHERE first_name IS NOT NULL;
UPDATE users SET user_type = 'ADMIN' WHERE id = 1;

INSERT INTO payuclientCredentials(client_id, client_secret, merchant_pos_id, shelter_id) VALUES ('463117', '913d4d4fc69fdadceb45f3469fc89341', '463117', 2);
INSERT INTO payuclientCredentials(client_id, client_secret, merchant_pos_id, shelter_id) VALUES ('464332', '90f4f19621b3ca839378add644486b0f', '464332', 3);

INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Max','Friendly and loves to play fetch','DOG','MALE','ADULT','2019-01-01','READY_FOR_ADOPTION',true,true,true,true,true,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Reksio','Friendly and loves to play fetch','DOG','MALE','ADULT','2019-01-01','READY_FOR_ADOPTION',true,true,true,true,true,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Bolo','Friendly and loves to play fetch','DOG','MALE','ADULT','2019-01-01','READY_FOR_ADOPTION',true,true,true,true,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Lucky','Very sociable and playful','DOG','MALE','VERY_YOUNG','2023-01-01','READY_FOR_ADOPTION',true,true,true,false,false,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Snow','Calm and good with kids','CAT','FEMALE','ADULT','2020-05-05','READY_FOR_ADOPTION',false,true,false,true,true,false,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Bella','Active and loves to run','DOG','FEMALE','ADULT','2019-06-01','READY_FOR_ADOPTION',true,false,true,false,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Coco','Quiet and loves to cuddle','CAT','MALE','SENIOR','2015-09-01','READY_FOR_ADOPTION',true,true,true,true,false,false,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Milo','Loves to play fetch','DOG','MALE','ADULT','2019-04-01','READY_FOR_ADOPTION',true,true,true,true,true,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Molly','Very affectionate','DOG','FEMALE','ADULT','2019-08-01','READY_FOR_ADOPTION',true,false,true,true,false,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Oliver','Sociable and cuddly','CAT','MALE','ADULT','2017-05-07','READY_FOR_ADOPTION',false,true,true,true,false,false,false,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Luna','Playful and energetic','DOG','FEMALE','VERY_YOUNG','2023-02-02','READY_FOR_ADOPTION',true,false,true,false,true,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Oscar','Quiet and loving','DOG','MALE','SENIOR','2015-08-08','READY_FOR_ADOPTION',true,true,true,true,false,false,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Daisy','Active and playful','DOG','FEMALE','ADULT','2021-07-07','READY_FOR_ADOPTION',false,true,false,true,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Toby','Friendly and good with children','DOG','MALE','ADULT','2018-04-04','READY_FOR_ADOPTION',true,true,true,true,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Poppy','Calm and loves to cuddle','CAT','FEMALE','SENIOR','2014-09-09','READY_FOR_ADOPTION',true,false,true,true,false,false,false,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Charlie','Friendly and loves playing with balls','DOG','MALE','VERY_YOUNG','2023-03-15','READY_FOR_ADOPTION',true,false,true,false,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Shadow','Independent and loves to explore','CAT','MALE','ADULT','2017-06-17','READY_FOR_ADOPTION',true,false,false,true,false,false,false,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Chloe','Active and enjoys long walks','DOG','FEMALE','ADULT','2021-08-21','READY_FOR_ADOPTION',false,true,true,true,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Tiger','Playful and good with kids','CAT','MALE','ADULT','2023-11-11','READY_FOR_ADOPTION',true,false,true,true,false,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Ruby','Calm and enjoys cuddles','DOG','FEMALE','ADULT','2019-07-07','READY_FOR_ADOPTION',true,true,true,false,false,true,true,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Jack','Loves playing fetch','DOG','MALE','VERY_YOUNG','2023-01-23','READY_FOR_ADOPTION',false,true,true,false,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Lola','Sociable and loves to play','DOG','FEMALE','VERY_YOUNG','2023-04-14','READY_FOR_ADOPTION',true,false,true,false,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Ollie','Adventurous and curious','CAT','MALE','ADULT','2017-07-19','READY_FOR_ADOPTION',false,true,false,true,false,false,false,true,2);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Buddy','Active and loves to run','DOG','MALE','ADULT','2021-09-12','READY_FOR_ADOPTION',true,false,true,true,true,true,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Misty','Quiet and good with kids','CAT','FEMALE','ADULT','2023-11-15','READY_FOR_ADOPTION',true,true,true,true,false,false,true,true,1);
INSERT INTO animals(name,information,species,sex,age,birth_date,animal_status,sterilized,vaccinated,kids_friendly,couch_potato,needs_activeness,cats_friendly,dogs_friendly,available_for_walk,user_id) VALUES ('Rocky','Loves to play fetch','DOG','MALE','ADULT','2019-07-17','READY_FOR_ADOPTION',false,true,true,false,true,true,true,true,1);

-- select * from users join user_roles on users.id=user_roles.user_id join roles on user_roles.role_id=roles.id;