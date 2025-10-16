package com.example.leave_request_workflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.leave_request_workflow.repository.UserRepository;
import org.springframework.ui.Model;

@Controller
public class UserController {

    // UserRepositoryを使うためのフィールドを定義
    // finalを付けることで「コンストラクタで必ず初期化される」ことを保証する
    private final UserRepository userRepository;

    // コンストラクタインジェクション
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
