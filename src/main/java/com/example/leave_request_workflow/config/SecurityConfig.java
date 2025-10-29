package com.example.leave_request_workflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // アクセス制御
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.formLogin(login -> login // フォーム認証を使う
                .loginPage("/login") // ログイン認証が必要な際はこのページを表示する
                .defaultSuccessUrl("/dashboard", true) // ログイン成功後の遷移先
                .permitAll())

                .authorizeHttpRequests(authz -> authz.requestMatchers("/css/**").permitAll() // CSSは認証不要
                        .requestMatchers("/").permitAll() // トップページは認証不要
                        .requestMatchers("/register").permitAll() // 新規ユーザー登録ページは認証不要
                        .requestMatchers("/h2-console/**").permitAll() // H2コンソールに全員アクセス許可
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated() // 他は認証必要
                )
                // フレームを使用できるようにする（H2コンソール表示用）
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // CSRF保護を無効化（H2コンソールへのPOSTを許可するため）
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }
}
