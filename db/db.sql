CREATE DATABASE quarkus-social;

CREATE TABLE "Users" (
id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
);

CREATE TABLE "Posts" (
id bigserial not null primary key,
post_text varchar(150) not null,
dateTime timestamp not null,
user_id bigint not null references "User"(id))

CREATE TABLE "Followers" (
	id bigserial not null primary key,
	user_id bigint not null references "Users"(id),
	follower_id bigint not null references "Users"(id)
)