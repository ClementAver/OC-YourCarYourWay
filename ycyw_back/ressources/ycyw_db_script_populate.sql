-- Employee user, you can use it to test the application.
INSERT INTO users (created_at, updated_at, email, name, password, role)
VALUES (
    NOW(6),
    NOW(6),
    'employee@mail.me',
    'employee',
    '$2a$10$97MSiG/g00DHzzlkZxxSJuPLwpnQF5svuHp7Ct03BPBlulR4fdA9u',
    'EMPLOYEE'
);

-- Optional customer user, as you can use the subscription feature to create one.
INSERT INTO users (created_at, updated_at, email, name, password, role)
VALUES (
    NOW(6),
    NOW(6),
    'customer@mail.me',
    'customer',
    '$2a$10$97MSiG/g00DHzzlkZxxSJuPLwpnQF5svuHp7Ct03BPBlulR4fdA9u',
    'CUSTOMER'
);