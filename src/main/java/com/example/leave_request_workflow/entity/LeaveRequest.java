package com.example.leave_request_workflow.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.leave_request_workflow.entity.enums.LeaveStatus;
import com.example.leave_request_workflow.entity.enums.LeaveType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "leave_request")
@Getter
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

    @Column(name = "reason")
    private String reason;

    // ステータス（Enum → DB上は文字列）
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private LeaveStatus status;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "admin_comment")
    private String adminComment;
}
