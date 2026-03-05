package com.emp.management.controller;

import com.emp.management.dto.request.CreateEmployeeRequest;
import com.emp.management.dto.request.EmployeeRequest;
import com.emp.management.dto.response.ApiResponse;
import com.emp.management.dto.response.EmployeeResponse;
import com.emp.management.service.EmployeeService;
import com.emp.management.util.SortFieldValidator;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.emp.management.entity.User;

/**
 * Employee management REST controller.
 * Admin can create, update, delete. Both roles can view.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // ===== Constructor Injection =====

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * POST /api/employees — Create a new employee (Admin only).
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        // Map to EmployeeRequest for the service layer
        EmployeeRequest empRequest = new EmployeeRequest();
        empRequest.setFirstName(request.getFirstName());
        empRequest.setLastName(request.getLastName());
        empRequest.setPhone(request.getPhone());
        empRequest.setDesignation(request.getDesignation());
        empRequest.setDateOfJoining(request.getDateOfJoining());
        empRequest.setSalary(request.getSalary());
        empRequest.setDepartmentId(request.getDepartmentId());
        empRequest.setActive(request.isActive());

        EmployeeResponse response = employeeService.createEmployee(
                empRequest, request.getEmail(), request.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", response));
    }

    /**
     * GET /api/employees — List all employees with pagination and optional search.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        Sort sort = SortFieldValidator.createSort("employee", sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmployeeResponse> employees = (search != null && !search.trim().isEmpty())
                ? employeeService.searchEmployees(search.trim(), pageable)
                : employeeService.getAllEmployees(pageable);

        return ResponseEntity.ok(ApiResponse.success("Employees retrieved", employees));
    }

    /**
     * GET /api/employees/{id} — Get a single employee by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved", response));
    }

    /**
     * PUT /api/employees/{id} — Update an employee (Admin only).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", response));
    }

    /**
     * DELETE /api/employees/{id} — Soft-delete (deactivate) an employee (Admin
     * only).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deactivated successfully"));
    }

    /**
     * POST /api/employees/{id}/photo — Upload a profile photo.
     */
    @PostMapping("/{id}/photo")
    public ResponseEntity<ApiResponse> uploadProfilePhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        EmployeeResponse response = employeeService.uploadProfilePhoto(id, file);
        return ResponseEntity.ok(ApiResponse.success("Profile photo uploaded", response));
    }

    /**
     * GET /api/employees/profile — Get own profile.
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getMyProfile(@AuthenticationPrincipal User user) {
        EmployeeResponse response = employeeService.getEmployeeByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", response));
    }

    /**
     * PUT /api/employees/profile — Update own profile.
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateMyProfile(
            @Valid @RequestBody com.emp.management.dto.request.ProfileUpdateRequest request,
            @AuthenticationPrincipal User user) {

        EmployeeResponse response = employeeService.updateMyProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    /**
     * POST /api/employees/profile/photo — Upload own profile photo.
     */
    @PostMapping("/profile/photo")
    public ResponseEntity<ApiResponse> updateMyProfilePhoto(
            @RequestParam("avatarUrl") String avatarUrl,
            @AuthenticationPrincipal User user) {

        EmployeeResponse response = employeeService.updateMyProfilePhoto(user.getId(), avatarUrl);
        return ResponseEntity.ok(ApiResponse.success("Avatar updated successfully", response));
    }

    /**
     * PUT /api/employees/profile/password — Change own password.
     */
    @PutMapping("/profile/password")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody com.emp.management.dto.request.ChangePasswordRequest request,
            @AuthenticationPrincipal User user) {

        employeeService.changePassword(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }
}
