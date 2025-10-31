-- ユーザー
INSERT INTO users(email, password, name, roles)
VALUES
('admin@example.com', '$2a$10$q08VsE/SN0No.2Ap62QsQOZa2yypv6reZkZ7C3oEABTy2xreKLRLW', 'admin', 'ROLE_ADMIN'),
('user_1@example.com', '$2a$10$q08VsE/SN0No.2Ap62QsQOZa2yypv6reZkZ7C3oEABTy2xreKLRLW', 'user_1', 'ROLE_USER'),
('user_2@example.com', '$2a$10$q08VsE/SN0No.2Ap62QsQOZa2yypv6reZkZ7C3oEABTy2xreKLRLW', 'user_2', 'ROLE_USER'),
('user_3@example.com', '$2a$10$q08VsE/SN0No.2Ap62QsQOZa2yypv6reZkZ7C3oEABTy2xreKLRLW', 'user_3', 'ROLE_USER');

-- 休暇申請データ
INSERT INTO leave_request (user_id, applied_at, start_date, end_date, leave_type, reason, status, approved_at, rejected_at, admin_comment)
VALUES
-- ▼ user_id=1: すべての休暇種別＋ステータスを網羅
(1, NOW(), '2025-11-05', '2025-11-06', 'PAID',   '旅行のため',      'PENDING',   NULL, NULL, NULL),
(1, NOW(), '2025-10-10', '2025-10-12', 'SICK',   '風邪のため',      'APPROVED',  '2025-10-09 10:00:00', NULL, 'お大事にしてください。'),
(1, NOW(), '2025-09-15', '2025-09-16', 'UNPAID', '私用のため',      'REJECTED',  NULL, '2025-09-14 15:30:00', '業務都合により却下しました。'),
(1, NOW(), '2025-08-20', '2025-08-20', 'PAID',   '家庭の事情',      'WITHDRAWN', NULL, NULL, '本人取り下げ済み'),
-- ▼ user_id=2: 一般ユーザー（一覧表示用）
(2, NOW(), '2025-11-10', '2025-11-12', 'PAID', '旅行のため', 'PENDING', NULL, NULL, NULL),
(2, NOW(), '2025-11-18', '2025-11-18', 'SICK', '体調不良のため', 'APPROVED', '2025-11-17 09:00:00', NULL, '承認しました'),
-- ▼ user_id=3: 一般ユーザー（一覧表示用）
(3, NOW(), '2025-11-15', '2025-11-15', 'UNPAID', '家族の用事', 'PENDING', NULL, NULL, NULL),
(3, NOW(), '2025-11-20', '2025-11-22', 'PAID', '旅行', 'APPROVED', '2025-11-19 10:30:00', NULL, '問題ありません');
