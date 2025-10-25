package com.example.leave_request_workflow.exception;

/**
 * 現在のパスワードが一致しない場合にスローされる例外
 */
public class CurrentPasswordIncorrectException extends RuntimeException {
    public CurrentPasswordIncorrectException(String message) {
        super(message);
    }
}
