package com.example.leave_request_workflow.form;

import java.time.LocalDate;
import com.example.leave_request_workflow.entity.enums.LeaveType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LeaveRequestForm {

    @NotNull(message = "開始日は必須です。")
    private LocalDate startDate; // 休暇開始日

    @NotNull(message = "終了日は必須です。")
    private LocalDate endDate; // 休暇終了日

    @NotNull(message = "休暇種別は必須です。")
    private LeaveType leaveType; // 休暇種別（Enum）

    @NotBlank(message = "理由を入力してください。")
    @Size(max=255, message="理由は255文字以内で入力してください")
    private String reason; // 理由
}
