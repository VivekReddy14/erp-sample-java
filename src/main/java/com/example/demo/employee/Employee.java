package com.example.demo.employee;

import com.example.demo.department.Department;
import com.example.demo.team.Team;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 20)
    private String mobile;

    @Column(length = 100)
    private String personalEmail;

    @Column(length = 100, unique = true)
    private String workEmail;

    private LocalDate joinDate;

    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> subordinates = new HashSet<>();

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Employee() {}

    // getters and setters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getMobile() { return mobile; }
    public String getPersonalEmail() { return personalEmail; }
    public String getWorkEmail() { return workEmail; }
    public LocalDate getJoinDate() { return joinDate; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Department getDepartment() { return department; }
    public Team getTeam() { return team; }
    public Employee getManager() { return manager; }
    public Set<Employee> getSubordinates() { return subordinates; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setDepartment(Department department) { this.department = department; }
    public void setTeam(Team team) { this.team = team; }
    public void setManager(Employee manager) { this.manager = manager; }
    public void setStatus(String status) { this.status = status; }
}
