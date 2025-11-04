package com.example.leave_request_workflow.config;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.example.leave_request_workflow.entity.LeaveRequest;
import com.example.leave_request_workflow.entity.enums.LeaveStatus;
import com.example.leave_request_workflow.form.LeaveRequestForm;
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

    /**
     * 休暇申請を新規登録
     */
    public LeaveRequest createLeaveRequest(Integer userId, LeaveRequestForm form) {
        // 開始日と終了日のチェック
        if (form.getStartDate().isAfter(form.getEndDate())) {
            throw new IllegalArgumentException("開始日は終了日以前である必要があります");
        }
        // 新しい休暇申請エンティティを生成
        LeaveRequest leaveRequest = new LeaveRequest(userId, form.getStartDate(), form.getEndDate(),
                form.getLeaveType(), form.getReason());
        // DBに保存し、保存した申請のIDを返す
        return leaveRequestRepository.save(leaveRequest);
    }

    /**
     * 休暇申請取り下げ処理
     */
    public void withdrawLeaveRequest(Integer id, Integer userId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("指定された休暇申請データにアクセスする権限がありません。"));
        // ステータスがPENDINGであることを確認
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("この申請は取り下げできません。");
        }
        // ステータスをWITHDRAWNに変更
        leaveRequest.updateStatus(LeaveStatus.WITHDRAWN);
        leaveRequestRepository.save(leaveRequest);
    }

    /**
     * 休暇申請削除処理
     */
    public void deleteLeaveRequest(Integer id, Integer userId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("指定された休暇申請データにアクセスする権限がありません。"));
        // ステータスがWITHDRAWNであることを確認
        if (leaveRequest.getStatus() != LeaveStatus.WITHDRAWN) {
            throw new IllegalArgumentException("この申請は削除できません。");
        }
        // DBから完全削除
        leaveRequestRepository.delete(leaveRequest);
    }

    /**
     * 全ユーザーの休暇申請一覧を取得（管理者用）
     */
    public Page<LeaveRequest> getAllLeaveRequests(LeaveStatus status, Pageable pageable) {
        // ステータスフィルタあり
        if (status != null) {
            return leaveRequestRepository.findByStatus(status, pageable);
        }
        // ステータスフィルタなし
        return leaveRequestRepository.findAll(pageable);
    }

    /**
     * 管理者向け休暇申請詳細取得処理
     */
    public LeaveRequest getLeaveRequestByIdForAdmin(Integer id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定された休暇申請が見つかりません。"));
    }
}
