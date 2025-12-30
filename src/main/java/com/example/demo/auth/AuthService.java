package com.example.demo.auth;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.LoginResponse;
import com.example.demo.employee.Employee;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }
        System.out.println(passwordEncoder.getClass().getName());

        System.out.print(request.getPassword());

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        Employee emp = user.getEmployee();
        if (emp == null) {
            throw new RuntimeException("No employee linked to this user");
        }

        Long departmentId = emp.getDepartment() != null ? emp.getDepartment().getId() : null;
        String departmentName = emp.getDepartment() != null ? emp.getDepartment().getName() : null;

        Long teamId = emp.getTeam() != null ? emp.getTeam().getId() : null;
        String teamName = emp.getTeam() != null ? emp.getTeam().getName() : null;

        List<UserRole> roles = userRoleRepository.findByUserId(user.getId());
        List<String> roleNames = roles.stream()
                .map(r -> r.getRole().getName())
                .distinct()
                .toList();

        String token = jwtService.generateToken(user.getId(), user.getUsername(), emp.getId(), roleNames);

        return new LoginResponse(
                token,
                jwtService.getExpiryEpochSecondsFromNow(),
                user.getId(),
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getDateOfBirth(),
                emp.getWorkEmail(),
                emp.getMobile(),
                departmentId,
                departmentName,
                teamId,
                teamName,
                roleNames
        );

    }
}
