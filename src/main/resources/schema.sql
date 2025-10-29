-- ユーザー
DROP TABLE IF EXISTS users; -- 既存のテーブルを削除する
CREATE TABLE users (
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT,
    email           VARCHAR(100)    UNIQUE NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    roles           VARCHAR(100)
);