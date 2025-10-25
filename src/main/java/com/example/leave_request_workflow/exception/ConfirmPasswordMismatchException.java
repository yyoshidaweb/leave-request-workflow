package com.example.leave_request_workflow.exception;

/**
 * 新しいパスワードと確認用パスワードが一致しない場合にスローされる例外
 */
public class ConfirmPasswordMismatchException extends RuntimeException {
    public ConfirmPasswordMismatchException(String message) {
        super(message);
    }
}
