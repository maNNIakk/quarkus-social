CREATE DATABASE quarkus-social;

CREATE TABLE "Users" (
    id bigserial NOT NULL PRIMARY KEY,
    name varchar(100) NOT NULL,
    age integer NOT NULL
);

CREATE TABLE "Posts" (
    id bigserial NOT NULL PRIMARY KEY,
    post_text varchar(150) NOT NULL,
    dateTime timestamp NOT NULL,
    user_id bigint NOT NULL REFERENCES "Users"(id) ON DELETE CASCADE
);

CREATE TABLE "Followers" (
    id bigserial NOT NULL PRIMARY KEY,
    user_id bigint NOT NULL REFERENCES "Users"(id) ON DELETE CASCADE,
    follower_id bigint NOT NULL REFERENCES "Users"(id) ON DELETE CASCADE
);