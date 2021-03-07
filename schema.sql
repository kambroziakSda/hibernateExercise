create sequence hibernate_sequence start with 1 increment by 1

    create table Student (
       id integer not null,
        city varchar(255),
        street varchar(255),
        name varchar(255),
        lastName varchar(255),
        primary key (id)
    )
