package com.example.leave_request_workflow.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailForm {
    @Email(message = "メールアドレスの形式が正しくありません")
    @NotBlank(message = "メールアドレスを入力してください")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください。")
    private String email;
}
