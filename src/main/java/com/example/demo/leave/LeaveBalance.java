package com.example.demo.leave;

import com.example.demo.employee.Employee;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "leave_balance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "leave_type_id", "year"})
        }
)
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal available = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal used = BigDecimal.ZERO;

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public LeaveBalance() {}

    public Long getId() { return id; }
    public Employee getEmployee() { return employee; }
    public LeaveType getLeaveType() { return leaveType; }
    public int getYear() { return year; }
    public BigDecimal getAvailable() { return available; }
    public BigDecimal getUsed() { return used; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; }
    public void setYear(int year) { this.year = year; }
    public void setAvailable(BigDecimal available) { this.available = available; }
    public void setUsed(BigDecimal used) { this.used = used; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
