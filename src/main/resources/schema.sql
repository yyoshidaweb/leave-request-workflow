-- ユーザー
DROP TABLE IF EXISTS users; -- 既存のテーブルを削除する
CREATE TABLE users (
    id              BIGINT          PRIMARY KEY AUTO_INCREMENT,
    email           VARCHAR(100)    UNIQUE NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    roles           VARCHAR(100)
);

-- 休暇申請データ
DROP TABLE IF EXISTS leave_request; -- 既存のテーブルを削除する
CREATE TABLE leave_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    applied_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 申請日時（自動設定）
    start_date DATE NOT NULL,                      -- 休暇開始日
    end_date DATE NOT NULL,                        -- 休暇終了日
    leave_type VARCHAR(50) NOT NULL,               -- 休暇種別（Enum: PAID/SICK/UNPAID）
    reason VARCHAR(255),                           -- 理由
    status VARCHAR(50) NOT NULL,                   -- ステータス（Enum: PENDING/APPROVED/REJECTED/WITHDRAWN）
    approved_at DATETIME,                          -- 承認日時
    rejected_at DATETIME,                          -- 却下日時
    admin_comment VARCHAR(255),                    -- 管理者コメント
    CONSTRAINT fk_leave_request_user FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);