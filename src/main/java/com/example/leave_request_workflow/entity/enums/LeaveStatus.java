package com.example.leave_request_workflow.entity.enums;

// ステータスのEnum
public enum LeaveStatus {
    PENDING("承認待ち"), APPROVED("承認済み"), REJECTED("却下"), WITHDRAWN("取り下げ");

    private final String label;

    // コンストラクタで日本語ラベルを設定
    LeaveStatus(String label) {
        this.label = label;
    }

    // 日本語名を返す
    public String getLabel() {
        return label;
    }
}
