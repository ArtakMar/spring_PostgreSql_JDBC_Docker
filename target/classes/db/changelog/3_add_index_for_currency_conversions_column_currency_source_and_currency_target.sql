-- liquibase formatted sql

--changeset artak
--comment добавления indexa для currency_conversions (currency_source, currency_target)
CREATE index ON currency_conversions (currency_source, currency_target);