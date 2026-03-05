package com.emp.management.service.impl;

import com.emp.management.config.JwtService;
import com.emp.management.dto.request.LoginRequest;
import com.emp.management.dto.request.RefreshTokenRequest;
import com.emp.management.dto.request.RegisterRequest;
import com.emp.management.dto.response.AuthResponse;
import com.emp.management.entity.Employee;
import com.emp.management.entity.RefreshToken;
import com.emp.management.entity.User;
import com.emp.management.enums.Role;
import com.emp.management.exception.BadRequestException;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.repository.RefreshTokenRepository;
import com.emp.management.repository.UserRepository;
import com.emp.management.service.AuthService;
import com.emp.management.service.EmailService;
import com.emp.management.service.LeaveBalanceService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication service implementation — handles registration and login.
 * Creates both User and Employee records during registration.
 */
@Service
public class AuthServiceImpl implements AuthService {

        private final UserRepository userRepository;
        private final EmployeeRepository employeeRepository;
        private final RefreshTokenRepository refreshTokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final EmailService emailService;
        private final LeaveBalanceService leaveBalanceService;

        // ===== Constructor Injection =====

        public AuthServiceImpl(UserRepository userRepository, EmployeeRepository employeeRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, EmailService emailService, LeaveBalanceService leaveBalanceService) {
                this.userRepository = userRepository;
                this.employeeRepository = employeeRepository;
                this.refreshTokenRepository = refreshTokenRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
                this.authenticationManager = authenticationManager;
                this.emailService = emailService;
                this.leaveBalanceService = leaveBalanceService;
        }

        @Override
        @Transactional
        public AuthResponse register(RegisterRequest request) {
                // Check if email already exists
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new BadRequestException("Email already registered: " + request.getEmail());
                }

                // Create user entity
                User user = new User(null, request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getRole() != null ? request.getRole() : Role.EMPLOYEE, true, null, null);
                user = userRepository.save(user);

                // Create employee profile
                Employee employee = new Employee(null, request.getFirstName(), request.getLastName(), request.getPhone(), null, null, null, null, null, null, null, null, null, null, null, true, user, null, null, null);
                employee = employeeRepository.save(employee);

                // Initialize leave balances for new employee
                leaveBalanceService.initializeLeaveBalances(employee.getId());

                // Generate JWT token with role claim
                String token = generateTokenWithClaims(user, employee);

                // Generate and save refresh token
                String refreshToken = jwtService.generateRefreshToken(user);
                saveRefreshToken(user, refreshToken);

                // Send welcome email asynchronously
                emailService.sendWelcomeEmail(user.getEmail(), employee.getFullName());

                return AuthResponse.builder()
                                .token(token)
                                .refreshToken(refreshToken)
                                .email(user.getEmail())
                                .role(user.getRole())
                                .userId(user.getId())
                                .employeeId(employee.getId())
                                .fullName(employee.getFullName())
                                .phone(employee.getPhone())
                                .profilePhoto(employee.getProfilePhoto())
                                .bio(employee.getBio())
                                .educationDegree(employee.getEducationDegree())
                                .educationInstitution(employee.getEducationInstitution())
                                .educationBranch(employee.getEducationBranch())
                                .educationPassingYear(employee.getEducationPassingYear())
                                .skills(employee.getSkills())
                                .address(employee.getAddress())
                                .build();
        }

        @Override
        public AuthResponse login(LoginRequest request) {
                // Authenticate credentials via Spring Security
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                // Load user and employee
                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new BadRequestException("User not found"));

                Employee employee = employeeRepository.findByUserId(user.getId())
                                .orElse(null);

                // Generate JWT token
                String token = generateTokenWithClaims(user, employee);

                // Generate and save refresh token
                String refreshToken = jwtService.generateRefreshToken(user);
                saveRefreshToken(user, refreshToken);

                return AuthResponse.builder()
                                .token(token)
                                .refreshToken(refreshToken)
                                .email(user.getEmail())
                                .role(user.getRole())
                                .userId(user.getId())
                                .employeeId(employee != null ? employee.getId() : null)
                                .fullName(employee != null ? employee.getFullName() : user.getEmail())
                                .phone(employee != null ? employee.getPhone() : null)
                                .profilePhoto(employee != null ? employee.getProfilePhoto() : null)
                                .bio(employee != null ? employee.getBio() : null)
                                .educationDegree(employee != null ? employee.getEducationDegree() : null)
                                .educationInstitution(employee != null ? employee.getEducationInstitution() : null)
                                .educationBranch(employee != null ? employee.getEducationBranch() : null)
                                .educationPassingYear(employee != null ? employee.getEducationPassingYear() : null)
                                .skills(employee != null ? employee.getSkills() : null)
                                .address(employee != null ? employee.getAddress() : null)
                                .build();
        }

        @Override
        public AuthResponse refreshToken(RefreshTokenRequest request) {
                // Validate refresh token
                RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

                if (refreshToken.isExpired()) {
                        refreshTokenRepository.delete(refreshToken);
                        throw new BadRequestException("Refresh token has expired");
                }

                User user = refreshToken.getUser();
                Employee employee = employeeRepository.findByUserId(user.getId()).orElse(null);

                // Generate new access token
                String newAccessToken = generateTokenWithClaims(user, employee);

                // Generate new refresh token
                String newRefreshToken = jwtService.generateRefreshToken(user);
                refreshTokenRepository.delete(refreshToken);
                saveRefreshToken(user, newRefreshToken);

                return AuthResponse.builder()
                                .token(newAccessToken)
                                .refreshToken(newRefreshToken)
                                .email(user.getEmail())
                                .role(user.getRole())
                                .userId(user.getId())
                                .employeeId(employee != null ? employee.getId() : null)
                                .fullName(employee != null ? employee.getFullName() : user.getEmail())
                                .phone(employee != null ? employee.getPhone() : null)
                                .profilePhoto(employee != null ? employee.getProfilePhoto() : null)
                                .bio(employee != null ? employee.getBio() : null)
                                .educationDegree(employee != null ? employee.getEducationDegree() : null)
                                .educationInstitution(employee != null ? employee.getEducationInstitution() : null)
                                .educationBranch(employee != null ? employee.getEducationBranch() : null)
                                .educationPassingYear(employee != null ? employee.getEducationPassingYear() : null)
                                .skills(employee != null ? employee.getSkills() : null)
                                .address(employee != null ? employee.getAddress() : null)
                                .build();
        }

        @Override
        public void logout(String token) {
                RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
                refreshTokenRepository.delete(refreshToken);
        }

        /**
         * Generate JWT with role and employeeId as extra claims.
         */
        private String generateTokenWithClaims(User user, Employee employee) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("role", user.getRole().name());
                if (employee != null) {
                        claims.put("employeeId", employee.getId());
                }
                return jwtService.generateToken(claims, user);
        }

        /**
         * Save refresh token in database with 7-day expiration.
         */
        private void saveRefreshToken(User user, String token) {
                RefreshToken refreshToken = new RefreshToken(null, user, token, LocalDateTime.now().plusDays(7), LocalDateTime.now());
                refreshTokenRepository.save(refreshToken);
        }
}
