package com.example.leave_request_workflow.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.leave_request_workflow.entity.enums.LeaveStatus;
import com.example.leave_request_workflow.entity.enums.LeaveType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leave_request")
@Getter
@NoArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 外部キー: users.id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // 休暇種別（Enum → DB上は文字列）
    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false, length = 50)
    private LeaveType leaveType;

    @Column(name = "reason", length = 255)
    private String reason;

    // ステータス（Enum → DB上は文字列）
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private LeaveStatus status;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "admin_comment", length = 255)
    private String adminComment;

    // 休暇申請とユーザーを紐付け
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    /**
     * 新規登録用コンストラクタ（初期ステータスはPENDING）
     */
    public LeaveRequest(Integer userId, LocalDate startDate, LocalDate endDate, LeaveType leaveType,
            String reason) {
        this.userId = userId; // ログインユーザーのIDを設定
        this.startDate = startDate; // 開始日
        this.endDate = endDate; // 終了日
        this.leaveType = leaveType; // 休暇種別
        this.reason = reason; // 理由
        this.appliedAt = LocalDateTime.now(); // 申請日時
        this.status = LeaveStatus.PENDING; // 初期ステータスをPENDINGに設定
    }

    /**
     * ステータス更新用メソッド
     */
    public void updateStatus(LeaveStatus status) {
        this.status = status;
    }

    /**
     * 承認時刻の更新メソッド
     */
    public void updateApprovedAtNow() {
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * 却下時刻更新メソッド
     */
    public void updateRejectAtNow() {
        this.rejectedAt = LocalDateTime.now();
    }

    /**
     * 管理者コメント更新メソッド
     */
    public void updateAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }
}
