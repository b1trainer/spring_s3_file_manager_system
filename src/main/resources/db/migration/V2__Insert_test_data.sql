INSERT INTO users (username, password, role, status)
VALUES ('admin', 'dmVyeXZlcnlzdHJvbmdzZWNyZXRwYXNzd29yZCE=', 'ADMIN', 'ACTIVE'),
       ('john_doe', 'dmVyeXZlcnlzdHJvbmdzZWNyZXRwYXNzd29yZCE=', 'USER', 'ACTIVE'),
       ('jane_smith', 'dmVyeXZlcnlzdHJvbmdzZWNyZXRwYXNzd29yZCE=', 'USER', 'ACTIVE'),
       ('bob_wilson', 'dmVyeXZlcnlzdHJvbmdzZWNyZXRwYXNzd29yZCE=', 'MODERATOR', 'ACTIVE'),
       ('alice_brown', 'dmVyeXZlcnlzdHJvbmdzZWNyZXRwYXNzd29yZCE=', 'USER', 'BLOCKED'),
       ('charlie_davis', 'dmVyeXZlcnlzdHJvbmdzZWNyZXRwYXNzd29yZCE=', 'USER', 'BLOCKED'),
       ('emma_jones', 'dmVyeXZlcnlzdHJvbmdzZWNyZXRwYXNzd29yZCE=', 'USER', 'ACTIVE');

INSERT INTO files (name, location, status, user_id)
VALUES ('document.pdf', 'my-bucket/document.pdf', 'DELETED', 3),
       ('presentation.pptx', 'my-bucket/presentation.pptx', 'DELETED', 3);

INSERT INTO events (user_id, file_id, status, timestamp)
VALUES (3, 3, 'UPLOAD', '2024-01-16 09:20:00'),
       (3, 3, 'DELETE', '2024-01-18 12:15:00'),
       (3, 10, 'UPLOAD', '2024-01-22 09:10:00'),
       (3, 10, 'DELETE', '2024-01-24 16:30:00');