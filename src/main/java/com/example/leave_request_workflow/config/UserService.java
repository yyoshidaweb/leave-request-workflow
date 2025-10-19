package com.example.leave_request_workflow.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.leave_request_workflow.form.UserForm;
import com.example.leave_request_workflow.entity.User;
import com.example.leave_request_workflow.repository.UserRepository;

// 登録ロジック
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserForm form) {
        // パスワードを暗号化してから保存
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        User user = new User(form.getEmail(), encodedPassword, form.getName());
        userRepository.save(user);
    }
}
