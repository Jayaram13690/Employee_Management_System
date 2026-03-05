package com.emp.management.controller;

import com.emp.management.dto.request.DepartmentRequest;
import com.emp.management.dto.response.ApiResponse;
import com.emp.management.dto.response.DepartmentResponse;
import com.emp.management.dto.response.EmployeeResponse;
import com.emp.management.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Department management REST controller.
 * All operations are Admin-only except viewing.
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    // ===== Constructor Injection =====

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * POST /api/departments — Create a new department.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", response));
    }

    /**
     * GET /api/departments — List all departments.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved", departments));
    }

    /**
     * GET /api/departments/{id} — Get department by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDepartmentById(@PathVariable Long id) {
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved", response));
    }

    /**
     * PUT /api/departments/{id} — Update department.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", response));
    }

    /**
     * DELETE /api/departments/{id} — Delete department.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully"));
    }

    /**
     * GET /api/departments/{id}/employees — Get employees in a department.
     */
    @GetMapping("/{id}/employees")
    public ResponseEntity<ApiResponse> getEmployeesByDepartment(@PathVariable Long id) {
        List<EmployeeResponse> employees = departmentService.getEmployeesByDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department employees retrieved", employees));
    }
}
