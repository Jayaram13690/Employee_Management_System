package com.emp.management.service.impl;

import com.emp.management.dto.request.EmployeeRequest;
import com.emp.management.dto.response.EmployeeResponse;
import com.emp.management.entity.Department;
import com.emp.management.entity.Employee;
import com.emp.management.entity.User;
import com.emp.management.enums.Role;
import com.emp.management.exception.BadRequestException;
import com.emp.management.exception.ResourceNotFoundException;
import com.emp.management.repository.DepartmentRepository;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.repository.UserRepository;
import com.emp.management.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of EmployeeService — handles all employee CRUD operations.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== Constructor Injection =====

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserRepository userRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request, String email, String password) {
        // Validate unique email
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered: " + email);
        }

        // Create user account for the employee
        User user = new User(null, email, passwordEncoder.encode(password), Role.EMPLOYEE, request.isActive(), null, null);
        user = userRepository.save(user);

        // Resolve department if provided
        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department", "id", request.getDepartmentId()));
        }

        // Create employee record
        Employee employee = new Employee(null, request.getFirstName(), request.getLastName(), request.getPhone(), request.getDesignation(), request.getDateOfJoining(), request.getSalary(), null, request.getBio(), request.getEducationDegree(), request.getEducationInstitution(), request.getEducationBranch(), request.getEducationPassingYear(), request.getSkills(), request.getAddress(), request.isActive(), user, department, null, null);

        employee = employeeRepository.save(employee);
        System.out.println("Created employee: " + employee.getFullName() + " (ID: " + employee.getId() + ")");

        return mapToResponse(employee);
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAllExcludingRole(Role.ADMIN, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<EmployeeResponse> searchEmployees(String search, Pageable pageable) {
        return employeeRepository.searchEmployeesExcludingRole(search, Role.ADMIN, pageable).map(this::mapToResponse);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByUserId(Long userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "userId", userId));
        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = findEmployeeOrThrow(id);

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPhone(request.getPhone());
        employee.setDesignation(request.getDesignation());
        employee.setDateOfJoining(request.getDateOfJoining());
        employee.setSalary(request.getSalary());
        employee.setBio(request.getBio());
        employee.setEducationDegree(request.getEducationDegree());
        employee.setEducationInstitution(request.getEducationInstitution());
        employee.setEducationBranch(request.getEducationBranch());
        employee.setEducationPassingYear(request.getEducationPassingYear());
        employee.setSkills(request.getSkills());
        employee.setAddress(request.getAddress());
        employee.setActive(request.isActive());

        // Update department
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department", "id", request.getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        // Sync active status with user
        User user = employee.getUser();
        user.setActive(request.isActive());
        userRepository.save(user);

        employee = employeeRepository.save(employee);
        System.out.println("Updated employee: " + employee.getFullName() + " (ID: " + employee.getId() + ")");

        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeOrThrow(id);

        // Soft-delete: deactivate the user account
        User user = employee.getUser();
        user.setActive(false);
        userRepository.save(user);

        employee.setActive(false);
        employeeRepository.save(employee);

        System.out.println("Deactivated employee: " + employee.getFullName() + " (ID: " + employee.getId() + ")");
    }

    @Override
    @Transactional
    public EmployeeResponse uploadProfilePhoto(Long id, MultipartFile file) {
        Employee employee = findEmployeeOrThrow(id);

        // In a real app, save file to storage and store the URL
        String filename = "profile_" + id + "_" + file.getOriginalFilename();
        employee.setProfilePhoto(filename);
        employee = employeeRepository.save(employee);

        System.out.println("Uploaded profile photo for employee ID: " + id);
        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateMyProfile(Long userId, com.emp.management.dto.request.ProfileUpdateRequest request) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "userId", userId));

        // Users can only update their own profile fields, not sensitive data
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            employee.setPhone(request.getPhone());
        }
        employee.setBio(request.getBio());
        employee.setEducationDegree(request.getEducationDegree());
        employee.setEducationInstitution(request.getEducationInstitution());
        employee.setEducationBranch(request.getEducationBranch());
        employee.setEducationPassingYear(request.getEducationPassingYear());
        employee.setSkills(request.getSkills());
        employee.setAddress(request.getAddress());

        employee = employeeRepository.save(employee);
        System.out.println("Updated profile for employee: " + employee.getFullName() + " (ID: " + employee.getId() + ")");

        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateMyProfilePhoto(Long userId, String avatarUrl) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "userId", userId));

        employee.setProfilePhoto(avatarUrl);
        employee = employeeRepository.save(employee);

        System.out.println("Updated profile avatar for employee ID: " + employee.getId());
        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, com.emp.management.dto.request.ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        System.out.println("Password changed for user ID: " + userId);
    }

    // ===== Helper Methods =====

    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getUser().getEmail())
                .phone(employee.getPhone())
                .designation(employee.getDesignation())
                .dateOfJoining(employee.getDateOfJoining())
                .salary(employee.getSalary())
                .active(employee.isActive())
                .profilePhoto(employee.getProfilePhoto())
                .bio(employee.getBio())
                .educationDegree(employee.getEducationDegree())
                .educationInstitution(employee.getEducationInstitution())
                .educationBranch(employee.getEducationBranch())
                .educationPassingYear(employee.getEducationPassingYear())
                .skills(employee.getSkills())
                .address(employee.getAddress())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
