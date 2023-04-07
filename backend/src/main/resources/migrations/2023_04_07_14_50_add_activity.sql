--liquibase formatted sql
--changeset s20157:2023_04_07_14_50_add_activity.sql

CREATE TABLE IF NOT EXISTS activities
(
    id              SERIAL PRIMARY KEY,
    activity_type   VARCHAR(20),
    activity_time   TIMESTAMP,
    user_id         BIGINT NOT NULL,
    animal_id       BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (animal_id) REFERENCES animals (id)
);
