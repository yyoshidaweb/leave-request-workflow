package com.example.leave_request_workflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.leave_request_workflow.config.LoginUserDetails;
import com.example.leave_request_workflow.entity.LeaveRequest;
import com.example.leave_request_workflow.form.LeaveRequestForm;
import jakarta.validation.Valid;

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

    /**
     * 一般ユーザー向け詳細表示
     */
    @GetMapping("/user/leave-requests/{id}")
    public String showLeaveRequestDetail(@PathVariable Integer id,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails, Model model) {
        // ログイン中のユーザーIDを取得
        Integer userId = loginUserDetails.getId();
        // 休暇申請IDとユーザーIDが一致するデータのみ取得（他人の申請にアクセスした場合は403エラー）
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestByIdAndUser(id, userId);
        model.addAttribute("leaveRequest", leaveRequest);
        return "user/leave-request-detail";
    }

    /**
     * 休暇申請新規作成フォームの表示
     */
    @GetMapping("user/leave-requests/create")
    public String showCreateForm(Model model) {
        model.addAttribute("leaveRequestForm", new LeaveRequestForm()); // 空のフォームオブジェクトを渡す
        return "user/leave-request-create"; // Thymeleafのテンプレート名
    }

    /**
     * 休暇申請の新規作成処理
     */
    @PostMapping("user/leave-requests/create")
    public String createLeaveRequest(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
            @Valid @ModelAttribute LeaveRequestForm leaveRequestForm, BindingResult bindingResult,
            RedirectAttributes ra, Model model) {
        // 入力チェックでエラーがあればフォームに戻す
        if (bindingResult.hasErrors()) {
            return "user/leave-request-create";
        }
        // ログイン中のユーザーIDを取得
        Integer userId = loginUserDetails.getId();
        try {
            // サービス層で登録し、登録されたLeaveRequestを取得する
            LeaveRequest leaveRequest =
                    leaveRequestService.createLeaveRequest(userId, leaveRequestForm);
            // 成功メッセージをリダイレクト先に一度だけ渡す
            ra.addFlashAttribute("success", "休暇申請を登録しました。");
            // 作成された申請の詳細ページにリダイレクト
            return "redirect:/user/leave-requests/" + leaveRequest.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/leave-request-create";
        }
    }

    /**
     * 休暇申請の取り下げ処理
     */
    @PostMapping("/user/leave-requests/withdraw")
    public String withdrawLeaveRequest(@RequestParam Integer id,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails, RedirectAttributes ra) {
        // ログイン中のユーザーIDを取得
        Integer userId = loginUserDetails.getId();
        try {
            leaveRequestService.withdrawLeaveRequest(id, userId);
            ra.addFlashAttribute("success", "申請を取り下げました。");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/leave-requests/" + id;
    }
}
