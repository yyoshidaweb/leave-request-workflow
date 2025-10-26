package com.example.leave_request_workflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.leave_request_workflow.config.UserService;
import com.example.leave_request_workflow.form.UserForm;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    /**
     * コンストラクタ
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ユーザー一覧を表示（管理者のみ）
     */
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/index";
    }

    /**
     * ログイン画面を表示
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * ダッシュボード画面を表示
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    /**
     * 登録フォームの表示
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    /**
     * 登録処理
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userForm") UserForm form, BindingResult br,
            RedirectAttributes ra, Model model) {
        // バリデーションエラーがある場合は再度入力フォームを表示
        if (br.hasErrors()) {
            return "register";
        }
        userService.register(form);
        // 成功メッセージをリダイレクト先に一度だけ渡す
        ra.addFlashAttribute("success", "ユーザー登録が完了しました。");
        return "redirect:/login";
    }
}
