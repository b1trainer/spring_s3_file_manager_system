CREATE TABLE users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255)  NOT NULL UNIQUE,
    password VARCHAR(2048) NOT NULL,
    role     VARCHAR(32)   NOT NULL,
    status   VARCHAR(50)   NOT NULL,
    created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE files
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    location VARCHAR(500) NOT NULL,
    status   VARCHAR(50)  NOT NULL,
    user_id  INT          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE events
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    user_id   INT,
    file_id   INT,
    status    VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
    FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE SET NULL
);

CREATE INDEX idx_files_user_id ON files (user_id);
CREATE INDEX idx_events_user_id ON events (user_id);
CREATE INDEX idx_events_file_id ON events (file_id);
CREATE INDEX idx_events_timestamp ON events (timestamp);