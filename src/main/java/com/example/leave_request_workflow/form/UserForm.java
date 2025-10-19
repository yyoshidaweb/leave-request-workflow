package com.example.leave_request_workflow.form;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



// フォーム入力を受け取るためのDTO
@Data
public class UserForm {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}

