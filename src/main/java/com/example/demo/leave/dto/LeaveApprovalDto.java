package com.example.demo.leave.dto;

import java.time.Instant;

public class LeaveApprovalDto {

    private Long id;
    private String level;
    private String decision;

    private Long approverEmployeeId;
    private String approverName;

    private String note;
    private Instant decidedAt;

    public LeaveApprovalDto() {}

    public LeaveApprovalDto(
            Long id,
            String level,
            String decision,
            Long approverEmployeeId,
            String approverName,
            String note,
            Instant decidedAt
    ) {
        this.id = id;
        this.level = level;
        this.decision = decision;
        this.approverEmployeeId = approverEmployeeId;
        this.approverName = approverName;
        this.note = note;
        this.decidedAt = decidedAt;
    }

    public Long getId() { return id; }
    public String getLevel() { return level; }
    public String getDecision() { return decision; }
    public Long getApproverEmployeeId() { return approverEmployeeId; }
    public String getApproverName() { return approverName; }
    public String getNote() { return note; }
    public Instant getDecidedAt() { return decidedAt; }

    public void setId(Long id) { this.id = id; }
    public void setLevel(String level) { this.level = level; }
    public void setDecision(String decision) { this.decision = decision; }
    public void setApproverEmployeeId(Long approverEmployeeId) { this.approverEmployeeId = approverEmployeeId; }
    public void setApproverName(String approverName) { this.approverName = approverName; }
    public void setNote(String note) { this.note = note; }
    public void setDecidedAt(Instant decidedAt) { this.decidedAt = decidedAt; }
}
