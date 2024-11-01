create sequence hibernate_sequence start 1 increment 1;
create table reservations
(
    id       int8 not null,
    duration int4,
    resource varchar(255),
    start    timestamp,
    username varchar(255),
    primary key (id)
);
create table user_role
(
    username varchar(255) not null,
    roles    varchar(255)
);
create table users
(
    username varchar(255) not null,
    active   boolean,
    name     varchar(255),
    password varchar(255) not null,
    primary key (username)
);
alter table if exists reservations add constraint reservation_user_fk foreign key (username) references users;
alter table if exists user_role add constraint user_role_user_fk foreign key (username) references users;