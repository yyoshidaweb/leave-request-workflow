package com.example.leave_request_workflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.leave_request_workflow.config.LeaveRequestService;
import com.example.leave_request_workflow.config.LoginUserDetails;
import com.example.leave_request_workflow.entity.LeaveRequest;

@Controller
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    /**
     * 一般ユーザー向け一覧表示
     */
    @GetMapping("/user/leave-requests")
    public String listLeaveRequests(Model model,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails, Integer page) {
        // ログイン中のユーザーIDを取得
        Integer userId = loginUserDetails.getId();
        // ページ番号を取得（指定がなければ0ページ目）
        int currentPage = (page != null) ? page : 0;
        // ページネーション設定
        PageRequest pageable = PageRequest.of(currentPage, 5);
        // 該当ユーザーの休暇申請一覧を取得
        Page<LeaveRequest> leaveRequests =
                leaveRequestService.getLeaveRequestsByUser(userId, pageable);
        model.addAttribute("leaveRequests", leaveRequests);
        model.addAttribute("currentPage", currentPage);
        return "user/leave-request-list";
    }

}
