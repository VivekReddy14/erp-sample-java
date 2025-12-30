package com.example.demo.auth.dto;

import java.time.LocalDate;
import java.util.List;

public class LoginResponse {

    private String accessToken;
    private long expiresAtEpochSeconds;

    private Long userId;
    private Long employeeId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String workEmail;
    private String mobile;

    private Long departmentId;
    private String departmentName;

    private Long teamId;
    private String teamName;

    private List<String> roleNames;

    public LoginResponse() {}

    public LoginResponse(
            String accessToken,
            long expiresAtEpochSeconds,
            Long userId,
            Long employeeId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String workEmail,
            String mobile,
            Long departmentId,
            String departmentName,
            Long teamId,
            String teamName,
            List<String> roleNames
    ) {
        this.accessToken = accessToken;
        this.expiresAtEpochSeconds = expiresAtEpochSeconds;
        this.userId = userId;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.workEmail = workEmail;
        this.mobile = mobile;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.teamId = teamId;
        this.teamName = teamName;
        this.roleNames = roleNames;
    }

    public String getAccessToken() { return accessToken; }
    public long getExpiresAtEpochSeconds() { return expiresAtEpochSeconds; }
    public Long getUserId() { return userId; }
    public Long getEmployeeId() { return employeeId; }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getWorkEmail() { return workEmail; }
    public String getMobile() { return mobile; }

    public Long getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public Long getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }

    public List<String> getRoleNames() { return roleNames; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setExpiresAtEpochSeconds(long expiresAtEpochSeconds) { this.expiresAtEpochSeconds = expiresAtEpochSeconds; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public void setRoleNames(List<String> roleNames) { this.roleNames = roleNames; }
}
