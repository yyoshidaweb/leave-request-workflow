package com.example.leave_request_workflow.config;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.leave_request_workflow.entity.User;
import com.example.leave_request_workflow.repository.UserRepository;
import com.example.leave_request_workflow.form.UserForm;
import org.springframework.transaction.annotation.Transactional;
import com.example.leave_request_workflow.form.PasswordForm;
import com.example.leave_request_workflow.exception.CurrentPasswordIncorrectException;
import com.example.leave_request_workflow.exception.ConfirmPasswordMismatchException;
import com.example.leave_request_workflow.form.NameForm;
import com.example.leave_request_workflow.form.EmailForm;
import com.example.leave_request_workflow.form.RolesForm;

/** 
 * ビジネスロジックを担うサービス層
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 全ユーザーを取得
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 新規ユーザー登録処理
     */
    public void register(UserForm form) {
        // パスワードを暗号化してから保存
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        User user = new User(form.getEmail(), encodedPassword, form.getName());
        userRepository.save(user);
    }

    /**
     * IDでユーザーを検索
     */
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
    }

    /**
     * パスワード変更処理
     */
    @Transactional
    public void editPassword(Integer id, PasswordForm form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        // 現在のパスワードが一致するかチェック
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            // 独自例外をスローする
            throw new CurrentPasswordIncorrectException("現在のパスワードが正しくありません。");
        }
        // 新しいパスワードと再入力したパスワードが一致するかチェック
        if (form.getNewPassword() == null
                || !form.getNewPassword().equals(form.getConfirmPassword())) {
            // 独自例外をスローする
            throw new ConfirmPasswordMismatchException("確認用パスワードが一致しません。");
        }
        // 新しいパスワードを設定
        user.updatePassword(passwordEncoder.encode(form.getNewPassword()));
        // 保存
        userRepository.save(user);
    }

    /**
     * 名前変更処理
     */
    @Transactional
    public void editName(Integer id, NameForm form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        // 新しい名前を設定
        user.updateName(form.getName());
        // 保存
        userRepository.save(user);
    }

    /**
     * メールアドレス変更処理
     */
    @Transactional
    public void editEmail(Integer id, EmailForm form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        // 新しい名前を設定
        user.updateEmail(form.getEmail());
        // 保存
        userRepository.save(user);
    }

    /**
     * ロール変更処理
     */
    @Transactional
    public void editRoles(Integer id, RolesForm form) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        // 新しいロールを設定
        user.updateRoles(form.getRoles());
        // 保存
        userRepository.save(user);
    }
}
