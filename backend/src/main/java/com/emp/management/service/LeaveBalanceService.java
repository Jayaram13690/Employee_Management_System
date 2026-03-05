package com.emp.management.service;

import com.emp.management.dto.response.LeaveBalanceResponse;
import com.emp.management.enums.LeaveType;

import java.util.List;

/**
 * Service interface for leave balance management.
 */
public interface LeaveBalanceService {

    /**
     * Initialize leave balances for a new employee.
     */
    void initializeLeaveBalances(Long employeeId);

    /**
     * Get leave balance for specific leave type and year.
     */
    LeaveBalanceResponse getLeaveBalance(Long employeeId, LeaveType leaveType, int year);

    /**
     * Get all leave balances for an employee in a specific year.
     */
    List<LeaveBalanceResponse> getAllLeaveBalances(Long employeeId, int year);

    /**
     * Update used days when leave is approved.
     */
    void updateUsedDays(Long employeeId, LeaveType leaveType, int usedDays, int year);

    /**
     * Revert used days when leave is rejected.
     */
    void revertUsedDays(Long employeeId, LeaveType leaveType, int usedDays, int year);

    /**
     * Check if employee has sufficient leave balance.
     */
    boolean hasBalance(Long employeeId, LeaveType leaveType, int requestedDays, int year);
}
