package com.example.leave_request_workflow.config;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.leave_request_workflow.entity.LeaveRequest;
import com.example.leave_request_workflow.repository.LeaveRequestRepository;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    // 特定ユーザーの休暇申請一覧を取得
    public Page<LeaveRequest> getLeaveRequestsByUser(Integer userId, Pageable pageable) {
        return leaveRequestRepository.findByUserIdOrderByAppliedAtDesc(userId, pageable);
    }
}
