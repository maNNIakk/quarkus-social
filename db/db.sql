CREATE DATABASE quarkus-social;

CREATE TABLE "User" (
id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
);