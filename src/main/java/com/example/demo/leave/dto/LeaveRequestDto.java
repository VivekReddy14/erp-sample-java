package com.example.demo.leave.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class LeaveRequestDto {

    private Long id;

    private Long employeeId;
    private String employeeName;

    private Long leaveTypeId;
    private String leaveTypeCode;
    private String leaveTypeName;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal days;

    private String reason;
    private String status;

    private Instant submittedAt;
    private Instant decidedAt;

    private List<LeaveApprovalDto> approvals;

    public LeaveRequestDto() {}

    public Long getId() { return id; }
    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public Long getLeaveTypeId() { return leaveTypeId; }
    public String getLeaveTypeCode() { return leaveTypeCode; }
    public String getLeaveTypeName() { return leaveTypeName; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getDays() { return days; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public Instant getSubmittedAt() { return submittedAt; }
    public Instant getDecidedAt() { return decidedAt; }
    public List<LeaveApprovalDto> getApprovals() { return approvals; }

    public void setId(Long id) { this.id = id; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public void setLeaveTypeId(Long leaveTypeId) { this.leaveTypeId = leaveTypeId; }
    public void setLeaveTypeCode(String leaveTypeCode) { this.leaveTypeCode = leaveTypeCode; }
    public void setLeaveTypeName(String leaveTypeName) { this.leaveTypeName = leaveTypeName; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setDays(BigDecimal days) { this.days = days; }
    public void setReason(String reason) { this.reason = reason; }
    public void setStatus(String status) { this.status = status; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
    public void setDecidedAt(Instant decidedAt) { this.decidedAt = decidedAt; }
    public void setApprovals(List<LeaveApprovalDto> approvals) { this.approvals = approvals; }
}
