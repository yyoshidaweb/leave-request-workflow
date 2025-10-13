-- ユーザー
INSERT INTO users(email, password, name, roles)
VALUES
('admin@example.com', '$2a$10$q08VsE/SN0No.2Ap62QsQOZa2yypv6reZkZ7C3oEABTy2xreKLRLW', 'admin', 'ROLE_ADMIN'),
('user@example.com', '$2a$10$q08VsE/SN0No.2Ap62QsQOZa2yypv6reZkZ7C3oEABTy2xreKLRLW', 'user', 'ROLE_USER');