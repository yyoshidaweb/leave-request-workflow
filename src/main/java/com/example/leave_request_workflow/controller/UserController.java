package com.example.leave_request_workflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.leave_request_workflow.config.UserService;
import com.example.leave_request_workflow.form.UserForm;
import com.example.leave_request_workflow.repository.UserRepository;
import org.springframework.ui.Model;

@Controller
public class UserController {

    // UserRepositoryを使うためのフィールドを定義
    // finalを付けることで「コンストラクタで必ず初期化される」ことを保証する
    private final UserRepository userRepository;

    private final UserService userService;

    // コンストラクタインジェクション
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // ユーザー一覧を表示
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users/index";
    }

    // ログイン画面を表示
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ダッシュボード画面を表示
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // 登録フォームの表示
    @GetMapping("/register")
    public String showRegisterForm(@ModelAttribute("userForm") UserForm userForm) {
        return "register";
    }

    // 登録処理
    @PostMapping("/register")
    public String register(@ModelAttribute UserForm userForm) {
        userService.register(userForm);
        return "redirect:/login";
    }
}
