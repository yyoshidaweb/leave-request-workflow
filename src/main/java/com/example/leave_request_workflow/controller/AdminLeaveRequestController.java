package com.example.leave_request_workflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.leave_request_workflow.config.LeaveRequestService;
import com.example.leave_request_workflow.entity.LeaveRequest;
import com.example.leave_request_workflow.entity.enums.LeaveStatus;
import com.example.leave_request_workflow.form.AdminCommentForm;
import jakarta.validation.Valid;

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

    /**
     * 管理者向け休暇申請詳細表示
     */
    @GetMapping("/admin/leave-requests/{id}")
    public String showLeaveRequestDetail(@PathVariable Integer id, Model model) {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestByIdForAdmin(id);
        model.addAttribute("leaveRequest", leaveRequest);
        return "admin/leave-request-detail";
    }

    /**
     * 管理者向け休暇申請承認処理
     */
    @PostMapping("/admin/leave-requests/approve")
    public String approveLeaveRequest(@RequestParam Integer id, RedirectAttributes ra,
            Model model) {
        try {
            leaveRequestService.approveLeaveRequest(id);
            ra.addFlashAttribute("success", "申請を承認しました。");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/leave-requests/" + id;
    }

    /**
     * 管理者向け休暇申請却下処理
     */
    @PostMapping("/admin/leave-requests/reject")
    public String rejectLeaveRequest(@RequestParam Integer id, RedirectAttributes ra, Model model) {
        try {
            leaveRequestService.rejectLeaveRequest(id);
            ra.addFlashAttribute("success", "申請を却下しました。");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/leave-requests/" + id;
    }

    /**
     * 管理者コメント編集フォームを表示
     */
    @GetMapping("/admin/leave-requests/{id}/edit-admin-comment")
    public String showEditAdminCommentForm(@PathVariable Integer id, Model model) {
        // フォームを初期化
        AdminCommentForm form = new AdminCommentForm();
        // 編集対象の休暇申請を取得
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestByIdForAdmin(id);
        // フォームにidをセット
        form.setId(id);
        // フォームに既存の管理者コメントをセット
        form.setAdminComment(leaveRequest.getAdminComment());
        model.addAttribute("form", form);
        return "admin/edit-admin-comment";
    }

    /**
     * 管理者コメント編集処理
     */
    @PostMapping("/admin/leave-requests/admin-comment")
    public String updateAdminComment(@RequestParam Integer id,
            @Valid @ModelAttribute AdminCommentForm form, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            // 最初の検証エラーのメッセージを取得
            String errorMessage = br.getAllErrors().get(0).getDefaultMessage();
            ra.addFlashAttribute("error", errorMessage);
            return "redirect:/admin/leave-requests/" + id + "/edit-admin-comment";
        }
        try {
            leaveRequestService.updateAdminComment(id, form);
            ra.addFlashAttribute("success", "管理者コメントを更新しました。");
            return "redirect:/admin/leave-requests/" + id;
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/leave-requests/" + id + "/edit-admin-comment";
        }
    }
}
