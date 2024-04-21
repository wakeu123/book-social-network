CREATE DATABASE `book_social_network`;

create table `bcp_user`(
    `id` bigint NOT NULL,
    `firstname` VARCHAR(50),
    `lastname` VARCHAR(50),
    `dateOfBirth` DATE,
    `email` VARCHAR(50),
    `password` VARCHAR(60),
    `enabled` BOOLEAN DEFAULT FALSE,
    `accountLocked` BOOLEAN DEFAULT FALSE,
    `createdDate` DATE NOT NULL,
    `lastModifiedDate` DATE,
    PRIMARY KEY (`id`)
);
CREATE SEQUENCE user_seq BEGIN WITH 1 INCREMENT BY 1;