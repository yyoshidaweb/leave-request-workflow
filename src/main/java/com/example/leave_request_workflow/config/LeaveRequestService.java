package com.example.leave_request_workflow.config;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

    /**
     * 指定されたIDとユーザーIDの休暇申請を1件取得（本人以外は例外）
     */
    public LeaveRequest getLeaveRequestByIdAndUser(Integer id, Integer userId) {
        // IDとuserId両方で検索
        Optional<LeaveRequest> optionalRequest =
                leaveRequestRepository.findByIdAndUserId(id, userId);
        // データが存在しなければアクセス拒否（403エラー）
        return optionalRequest
                .orElseThrow(() -> new AccessDeniedException("指定された休暇申請データにアクセスする権限がありません。"));
    }
}
