--liquibase formatted sql

--changeset kutman:schemas-1
CREATE SCHEMA payment;

--changeset daivanov:schemas-2
CREATE SCHEMA IF NOT EXISTS payment;
