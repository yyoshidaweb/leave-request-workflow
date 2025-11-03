package com.example.leave_request_workflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.leave_request_workflow.config.LeaveRequestService;
import com.example.leave_request_workflow.entity.LeaveRequest;
import com.example.leave_request_workflow.entity.enums.LeaveStatus;

/**
 * 管理者向け休暇申請コントローラー
 */
@Controller
public class AdminLeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public AdminLeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    /**
     * 全ユーザーの休暇申請一覧表示（管理者専用）
     */
    @GetMapping("/admin/leave-requests")
    public String listAllLeaveRequests(@RequestParam(required = false) LeaveStatus status,
            @RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageable = PageRequest.of(page, 10);
        Page<LeaveRequest> leaveRequests =
                leaveRequestService.getAllLeaveRequests(status, pageable);
        model.addAttribute("leaveRequests", leaveRequests);
        model.addAttribute("currentPage", page);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", LeaveStatus.values());
        return "admin/leave-request-list";
    }
}
