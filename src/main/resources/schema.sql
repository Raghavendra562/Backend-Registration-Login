-- Reference schema. The backend auto-creates these tables via Hibernate (spring.jpa.hibernate.ddl-auto=update).

CREATE DATABASE IF NOT EXISTS registrationdb;
USE registrationdb;

CREATE TABLE IF NOT EXISTS `USER` (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(100) NOT NULL,
    phone    VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS JWTTOKEN (
    token_id   BIGINT        NOT NULL AUTO_INCREMENT,
    user_id    BIGINT        NOT NULL,
    token      VARCHAR(1000) NOT NULL,
    created_at DATETIME      NOT NULL,
    expires_at DATETIME      NOT NULL,
    PRIMARY KEY (token_id)
) ENGINE=InnoDB;