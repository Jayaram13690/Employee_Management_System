package com.emp.management.service;

import com.emp.management.dto.response.DashboardResponse;
import com.emp.management.dto.response.EmployeeDashboardResponse;

/**
 * Service interface for Dashboard summary data.
 */
public interface DashboardService {

    DashboardResponse getAdminDashboard();

    EmployeeDashboardResponse getEmployeeDashboard(Long employeeId);
}
