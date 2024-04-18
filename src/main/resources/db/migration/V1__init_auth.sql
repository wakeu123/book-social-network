CREATE TABLE BCP_USER IF NOT EXISTS (
    id BIGINT NOT NULL,
        firstname VARCHAR(50),
        lastname VARCHAR(50),
        dateOfBirth DATE,
        email VARCHAR(50),
        password VARCHAR(60)
        enabled BOOLEAN DEFAULT FALSE,
        accountLocked BOOLEAN DEFAULT FALSE,
        createdDate DATE NOT NULL,
        lastModifiedDate DATE,
        UNIQUE KEY (email)
        PRIMARY KEY (id)
);
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE BCP_ROLE IF NOT EXISTS (
    id BIGINT NOT NULL,
        name VARCHAR(50) NOT NULL,
        createdDate DATE NOT NULL,
        lastModifiedDate DATE,
        UNIQUE KEY (name),
        PRIMARY KEY (id)
);
CREATE SEQUENCE role_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE BCP_TOKEN IF NOT EXISTS (
    id BIGINT NOT NULL,
        userToken VARCHAR(255) NOT NULL,
        createdAt DATE NOT NULL,
        expiredAt DATE NOT NULL,
        validatedAt DATE NOT NULL,
        userId BIGINT NOT NULL,
        PRIMARY KEY (id)
);
CREATE SEQUENCE token_seq START WITH 1 INCREMENT BY 1;