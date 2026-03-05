package com.emp.management.controller;

import com.emp.management.dto.response.ApiResponse;
import com.emp.management.dto.response.DashboardResponse;
import com.emp.management.dto.response.EmployeeDashboardResponse;
import com.emp.management.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Dashboard REST controller — provides summary data for admin and employee
 * views.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    // ===== Constructor Injection =====

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /api/dashboard/admin — Admin dashboard summary.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAdminDashboard() {
        DashboardResponse response = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard data", response));
    }

    /**
     * GET /api/dashboard/employee/{employeeId} — Employee dashboard summary.
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse> getEmployeeDashboard(@PathVariable Long employeeId) {
        EmployeeDashboardResponse response = dashboardService.getEmployeeDashboard(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Employee dashboard data", response));
    }
}
