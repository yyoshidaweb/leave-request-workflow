package com.example.leave_request_workflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor // JPAがエンティティを復元するために必要（lombokが空のコンストラクタを自動生成してくれる）
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // メールアドレスを一意に制限
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String roles;

    // 新規作成専用のコンストラクタ
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = "ROLE_USER"; // 初期値は常に一般ユーザー
    }

    /**
     * パスワード更新用メソッド
     */
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    /**
     * 名前更新用メソッド
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * メールアドレス更新用メソッド
     */
    public void updateEmail(String email) {
        this.email = email;
    }

}
