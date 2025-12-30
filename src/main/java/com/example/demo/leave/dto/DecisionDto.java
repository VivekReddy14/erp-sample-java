package com.example.demo.leave.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DecisionDto {

    @NotNull
    private Long leaveRequestId;

    @NotBlank
    private String note;

    public Long getLeaveRequestId() { return leaveRequestId; }
    public String getNote() { return note; }

    public void setLeaveRequestId(Long leaveRequestId) { this.leaveRequestId = leaveRequestId; }
    public void setNote(String note) { this.note = note; }
}
