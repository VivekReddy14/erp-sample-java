package com.example.demo.auth;

import com.example.demo.department.Department;
import com.example.demo.team.Team;
import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {

    public enum ScopeType {
        GLOBAL,
        DEPARTMENT,
        TEAM
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false, length = 20)
    private ScopeType scopeType = ScopeType.GLOBAL;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public UserRole() {}

    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public Role getRole() { return role; }
    public ScopeType getScopeType() { return scopeType; }
    public Department getDepartment() { return department; }
    public Team getTeam() { return team; }

    public void setId(Long id) { this.id = id; }
    public void setUser(AppUser user) { this.user = user; }
    public void setRole(Role role) { this.role = role; }
    public void setScopeType(ScopeType scopeType) { this.scopeType = scopeType; }
    public void setDepartment(Department department) { this.department = department; }
    public void setTeam(Team team) { this.team = team; }
}
