package com.example.leave_request_workflow.form;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理者による管理者コメント用フォーム
 */
@Data
public class AdminCommentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 管理者コメント（任意）
    @Size(max = 200, message = "管理者コメントは200文字以内で入力してください。")
    private String adminComment;
}
