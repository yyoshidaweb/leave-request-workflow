package com.example.leave_request_workflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
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
