package com.example.demo.leave;

import com.example.demo.employee.Employee;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "leave_approval",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"leave_request_id", "level"})
        }
)
public class LeaveApproval {

    public enum Level {
        TEAM,
        DEPARTMENT
    }

    public enum Decision {
        PENDING,
        APPROVED,
        REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Level level;

    @ManyToOne(optional = false)
    @JoinColumn(name = "approver_employee_id", nullable = false)
    private Employee approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Decision decision = Decision.PENDING;

    @Column(length = 500)
    private String note;

    private Instant decidedAt;

    public LeaveApproval() {}

    public Long getId() { return id; }
    public LeaveRequest getLeaveRequest() { return leaveRequest; }
    public Level getLevel() { return level; }
    public Employee getApprover() { return approver; }
    public Decision getDecision() { return decision; }
    public String getNote() { return note; }
    public Instant getDecidedAt() { return decidedAt; }

    public void setId(Long id) { this.id = id; }
    public void setLeaveRequest(LeaveRequest leaveRequest) { this.leaveRequest = leaveRequest; }
    public void setLevel(Level level) { this.level = level; }
    public void setApprover(Employee approver) { this.approver = approver; }
    public void setDecision(Decision decision) { this.decision = decision; }
    public void setNote(String note) { this.note = note; }
    public void setDecidedAt(Instant decidedAt) { this.decidedAt = decidedAt; }
}
