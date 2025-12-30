package com.example.demo.leave.dto;

import java.math.BigDecimal;

public class LeaveBalanceDto {

    private Long leaveTypeId;
    private String leaveTypeCode;
    private String leaveTypeName;

    private int year;
    private BigDecimal available;
    private BigDecimal used;

    public LeaveBalanceDto() {}

    public LeaveBalanceDto(
            Long leaveTypeId,
            String leaveTypeCode,
            String leaveTypeName,
            int year,
            BigDecimal available,
            BigDecimal used
    ) {
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeCode = leaveTypeCode;
        this.leaveTypeName = leaveTypeName;
        this.year = year;
        this.available = available;
        this.used = used;
    }

    public Long getLeaveTypeId() { return leaveTypeId; }
    public String getLeaveTypeCode() { return leaveTypeCode; }
    public String getLeaveTypeName() { return leaveTypeName; }
    public int getYear() { return year; }
    public BigDecimal getAvailable() { return available; }
    public BigDecimal getUsed() { return used; }

    public void setLeaveTypeId(Long leaveTypeId) { this.leaveTypeId = leaveTypeId; }
    public void setLeaveTypeCode(String leaveTypeCode) { this.leaveTypeCode = leaveTypeCode; }
    public void setLeaveTypeName(String leaveTypeName) { this.leaveTypeName = leaveTypeName; }
    public void setYear(int year) { this.year = year; }
    public void setAvailable(BigDecimal available) { this.available = available; }
    public void setUsed(BigDecimal used) { this.used = used; }
}
