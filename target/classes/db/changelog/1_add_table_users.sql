--liquibase formatted sql

--changeset artak
--comment добавление таблицы users
CREATE TABLE users
(
    id UUID PRIMARY KEY

);