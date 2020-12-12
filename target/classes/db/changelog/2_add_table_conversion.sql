--liquibase formatted sql

--changeset artak
--comment добавление таблицы conversion
CREATE TABLE currency_conversions
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         UUID references users (id),
    request_id      UUID UNIQUE    NOT NULL,
    amount_source   numeric(19, 6) NOT NULL,
    currency_source VARCHAR(9)     NOT NULL,
    currency_target VARCHAR(9)     NOT NULL,
    create_date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP
);
