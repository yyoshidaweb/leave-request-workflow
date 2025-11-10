package com.example.leave_request_workflow.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.leave_request_workflow.entity.User;
import com.example.leave_request_workflow.form.UserForm;
import com.example.leave_request_workflow.repository.UserRepository;
import jakarta.annotation.PostConstruct;


/**
 * アプリ起動時に自動で実行される
 */
@Component
public class DataInitializer {

    private final UserService userService;
    private final UserRepository userRepository;

    public DataInitializer(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * テスト用管理者ユーザーを追加する処理
     */
    @PostConstruct
    @Transactional
    public void init() {
        // 管理者用メールアドレス
        String adminEmail = "test-admin@example.com";
        // 既に対象の管理者が存在するか確認
        boolean isExistsAdmin = userRepository.findByEmail(adminEmail).isPresent();
        // 既に対象の管理者が存在しない場合は追加処理を実行する
        if (!isExistsAdmin) {
            // 新規登録フォームに値を設定
            UserForm form = new UserForm();
            form.setEmail(adminEmail);
            form.setPassword("password");
            form.setName("テスト用管理者");
            // 新規登録を実行
            userService.register(form);
            // 作成されたユーザーを取得
            User user = userRepository.findByEmail(form.getEmail())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            user.updateRoles("ROLE_ADMIN");
            userRepository.save(user);
        }
    }
}
