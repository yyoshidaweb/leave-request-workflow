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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        // 現在ログイン中のユーザー情報を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetails loginUserDetails = (LoginUserDetails) auth.getPrincipal();
        // ログイン中ユーザーのIDを取得
        Integer loginUserId = loginUserDetails.getId();
        // 自分自身のロールを変更しようとした場合は例外をスローする
        if (loginUserId.equals(id)) {
            throw new IllegalStateException("自分自身のロールは変更できません。");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
        // 新しいロールを設定
        user.updateRoles(form.getRoles());
        // 保存
        userRepository.save(user);
    }

    /**
     * 削除処理
     */
    @Transactional
    public void deleteById(Integer id) {
        // ユーザーが存在しない場合は例外をスローする
        if (!userRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("ユーザーが存在しません: id=" + id, 1);
        }
        userRepository.deleteById(id);
    }

    /**
     * メールアドレスをマスクする
     */
    public String maskEmail(String email) {
        // @前後で分割
        String[] parts = email.split("@");
        // @以前部分
        String local = parts[0];
        // @以降部分（ドメイン）
        String domain = parts[1];

        String maskedLocal = local.length() <= 2 ? local + "****" : local.substring(0, 2) + "****";
        String[] domainParts = domain.split("\\.", 2);
        String maskedDomain;
        if (domainParts.length == 2) {
            // ドメイン名部分をマスク
            String domainName = domainParts[0].length() <= 2 ? domainParts[0] + "****"
                    : domainParts[0].substring(0, 2) + "****";
            // TLD部分をマスク
            String tld = domainParts[1].length() <= 2 ? domainParts[1] + "****"
                    : domainParts[1].substring(0, 2) + "****";
            maskedDomain = domainName + "." + tld;
        } else {
            // 「.」がない場合はそのままマスク
            maskedDomain = domain.length() <= 2 ? domain + "****" : domain.substring(0, 2) + "****";
        }
        return maskedLocal + "@" + maskedDomain;
    }
}
