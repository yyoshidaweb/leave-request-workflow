package com.example.leave_request_workflow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.leave_request_workflow.entity.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // 指定ユーザーの休暇申請を取得（ページング対応）
    Page<LeaveRequest> findByUserIdOrderByAppliedAtDesc(Integer userId, Pageable pageable);
}
