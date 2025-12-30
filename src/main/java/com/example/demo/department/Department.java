package com.example.demo.department;

import com.example.demo.employee.Employee;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToOne
    @JoinColumn(name = "department_head_id")
    private Employee departmentHead;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @OneToMany(mappedBy = "department")
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "department")
    private Set<com.example.demo.team.Team> teams = new HashSet<>();

    public Department() {}

    // getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Employee getDepartmentHead() { return departmentHead; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Set<Employee> getEmployees() { return employees; }
    public Set<com.example.demo.team.Team> getTeams() { return teams; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDepartmentHead(Employee departmentHead) { this.departmentHead = departmentHead; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
