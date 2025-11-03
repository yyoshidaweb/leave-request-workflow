package com.example.leave_request_workflow.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.leave_request_workflow.entity.LeaveRequest;
import com.example.leave_request_workflow.entity.enums.LeaveStatus;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // 指定ユーザーの休暇申請を取得（ページング対応）
    Page<LeaveRequest> findByUserIdOrderByAppliedAtDesc(Integer userId, Pageable pageable);

    // 特定の申請IDとユーザーIDで検索（本人確認用）
    Optional<LeaveRequest> findByIdAndUserId(Integer id, Integer userId);

    // 管理者向け: ステータス指定で取得（ページング）
    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);
}
