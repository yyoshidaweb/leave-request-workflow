package com.example.leave_request_workflow.controller;

import com.example.leave_request_workflow.entity.User;
import com.example.leave_request_workflow.config.LoginUserDetails;
import com.example.leave_request_workflow.config.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import com.example.leave_request_workflow.exception.CurrentPasswordIncorrectException;
import com.example.leave_request_workflow.exception.ConfirmPasswordMismatchException;
import com.example.leave_request_workflow.form.PasswordForm;
import com.example.leave_request_workflow.form.NameForm;
import com.example.leave_request_workflow.form.EmailForm;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    /**
     * プロフィール表示
     */
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
            Model model) {
        User user = userService.findById(loginUserDetails.getId());
        // テスト用管理者かどうか判定
        boolean isTestAdmin = "test-admin@example.com".equals(user.getEmail());
        model.addAttribute("isTestAdmin", isTestAdmin);
        model.addAttribute("user", user);
        return "profile/index";
    }

    /**
     * パスワード変更ページを表示
     */
    @GetMapping("/profile/edit-password")
    public String showEditPasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "profile/edit-password";
    }

    /**
     * パスワード変更処理
     */
    @PostMapping("/profile/edit-password")
    public String updatePassword(@Valid @ModelAttribute("passwordForm") PasswordForm form,
            BindingResult br, @AuthenticationPrincipal LoginUserDetails loginUserDetails,
            RedirectAttributes ra, Model model) {
        // バリデーションエラーがある場合は再度入力フォームを表示
        if (br.hasErrors()) {
            return "profile/edit-password";
        }
        try {
            // 現在ログイン中のユーザーを特定し、パスワード変更処理を実行
            userService.editPassword(loginUserDetails.getId(), form);
        } catch (CurrentPasswordIncorrectException e) {
            // 現在のパスワードが一致しない場合にエラーメッセージをフォームに表示
            br.rejectValue("currentPassword", "error.passwordForm", e.getMessage());
            return "profile/edit-password";
        } catch (ConfirmPasswordMismatchException e) {
            // 再入力したパスワードが一致しない場合にエラーメッセージをフォームに表示
            br.rejectValue("confirmPassword", "error.passwordForm", e.getMessage());
            return "profile/edit-password";
        }
        // 成功メッセージをリダイレクト先に一度だけ渡す
        ra.addFlashAttribute("success", "パスワードを変更しました");
        return "redirect:/profile";
    }

    /**
     * 名前変更ページを表示
     */
    @GetMapping("/profile/edit-name")
    public String showEditNameForm(Model model) {
        // 名前変更フォームを初期化してテンプレートに渡す
        model.addAttribute("nameForm", new NameForm());
        return "profile/edit-name"; // 名前変更ページを表示
    }

    /**
     * 名前変更処理
     */
    @PostMapping("/profile/edit-name")
    public String updateName(@Valid @ModelAttribute("nameForm") NameForm form, BindingResult br,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails, RedirectAttributes ra) {
        // バリデーションエラーがある場合は再度入力フォームを表示
        if (br.hasErrors()) {
            return "profile/edit-name";
        }
        // 現在ログイン中のユーザーを特定し、名前変更処理を実行
        userService.editName(loginUserDetails.getId(), form);
        // 成功メッセージをリダイレクト先に一度だけ渡す
        ra.addFlashAttribute("success", "名前を変更しました。");
        return "redirect:/profile";
    }

    /**
     * メールアドレス変更ページを表示
     */
    @GetMapping("/profile/edit-email")
    public String showEditEmailForm(Model model) {
        // メールアドレス変更フォームを初期化してテンプレートに渡す
        model.addAttribute("emailForm", new EmailForm());
        return "profile/edit-email"; // メールアドレス変更ページを表示
    }

    /**
     * メールアドレス変更処理
     */
    @PostMapping("/profile/edit-email")
    public String updateEmail(@Valid @ModelAttribute("emailForm") EmailForm form, BindingResult br,
            @AuthenticationPrincipal LoginUserDetails loginUserDetails, RedirectAttributes ra) {
        // バリデーションエラーがある場合は再度入力フォームを表示
        if (br.hasErrors()) {
            return "profile/edit-email";
        }
        // 現在ログイン中のユーザーを特定し、メールアドレス変更処理を実行
        userService.editEmail(loginUserDetails.getId(), form);
        // 成功メッセージをリダイレクト先に一度だけ渡す
        ra.addFlashAttribute("success", "メールアドレスを変更しました。");
        return "redirect:/profile";
    }

    /**
     * ユーザー削除処理
     */
    @PostMapping("/profile/delete")
    public String delete(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
            RedirectAttributes ra, HttpServletRequest request, HttpServletResponse response) {
        // ログイン中ユーザーのIDを取得
        Integer id = loginUserDetails.getId();
        try {
            // 現在ログイン中のユーザーを削除
            userService.deleteById(id);
            // ログアウト処理
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            // 成功メッセージを渡してログイン画面へ
            ra.addFlashAttribute("success", "アカウントを削除しました。");
            return "redirect:/login";
        } catch (EmptyResultDataAccessException e) {
            // すでに削除済みまたは存在しない場合
            ra.addFlashAttribute("error", "ユーザー情報が見つかりません。");
            return "redirect:/profile";
        }
    }
}
