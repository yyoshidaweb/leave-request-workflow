package com.example.leave_request_workflow.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordForm {
    @NotBlank(message = "現在のパスワードを入力してください。")
    private String currentPassword;

    @NotBlank(message = "新しいパスワードを入力してください。")
    @Size(min = 8, max = 255, message = "パスワードは8文字以上255文字以内で入力してください。")
    private String newPassword;

    @NotBlank(message = "新しいパスワードを再入力してください。")
    private String confirmPassword;
}
