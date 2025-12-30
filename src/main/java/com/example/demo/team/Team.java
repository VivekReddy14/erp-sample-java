package com.example.demo.team;

import com.example.demo.department.Department;
import com.example.demo.employee.Employee;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToOne
    @JoinColumn(name = "team_head_id")
    private Employee teamHead;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "team")
    private Set<Employee> employees = new HashSet<>();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Team() {}

    // getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Department getDepartment() { return department; }
    public Employee getTeamHead() { return teamHead; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Set<Employee> getEmployees() { return employees; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(Department department) { this.department = department; }
    public void setTeamHead(Employee teamHead) { this.teamHead = teamHead; }
}
