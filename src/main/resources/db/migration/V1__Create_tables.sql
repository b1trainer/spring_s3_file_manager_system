CREATE TABLE users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255)  NOT NULL,
    password VARCHAR(2048) NOT NULL,
    role     VARCHAR(32)   NOT NULL,
    status   VARCHAR(50)   NOT NULL,
    created  TIMESTAMP
);

CREATE TABLE roles
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    value VARCHAR(32) NOT NULL
);

CREATE TABLE users_roles
(
    user_id    INT NOT NULL,
    role_id    INT NOT NULL,
    CONSTRAINT user_id REFERENCES users (id),
    CONSTRAINT role_id REFERENCES roles (id)
);

CREATE TABLE files
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    location VARCHAR(500) NOT NULL,
    status   VARCHAR(50)  NOT NULL
);

CREATE TABLE events
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    user_id   INT,
    file_id   INT,
    status    VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (file_id) REFERENCES files (id)
);