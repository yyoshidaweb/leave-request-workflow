package com.example.leave_request_workflow.entity.enums;

// 休暇種別のEnum
public enum LeaveType {
    PAID("有給休暇"), SICK("病気休暇"), UNPAID("無給休暇");

    private final String label;

    LeaveType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
