package com.emp.management.service.impl;

import com.emp.management.dto.response.LeaveBalanceResponse;
import com.emp.management.entity.Employee;
import com.emp.management.entity.LeaveBalance;
import com.emp.management.enums.LeaveType;
import com.emp.management.exception.BadRequestException;
import com.emp.management.exception.ResourceNotFoundException;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.repository.LeaveBalanceRepository;
import com.emp.management.service.LeaveBalanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Leave balance service implementation.
 * Manages leave balance allocation and tracking.
 */
@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;

    // ===== Constructor Injection =====

    public LeaveBalanceServiceImpl(LeaveBalanceRepository leaveBalanceRepository, EmployeeRepository employeeRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Default annual leave allocation per type (can be made configurable)
    private static final int EARNED_LEAVE_ALLOCATION = 20;
    private static final int CASUAL_LEAVE_ALLOCATION = 12;
    private static final int SICK_LEAVE_ALLOCATION = 10;
    private static final int MATERNITY_LEAVE_ALLOCATION = 90;
    private static final int PATERNITY_LEAVE_ALLOCATION = 15;
    private static final int UNPAID_LEAVE_ALLOCATION = 5;

    @Override
    @Transactional
    public void initializeLeaveBalances(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        int currentYear = Year.now().getValue();

        // Create balance records for all leave types (idempotent operation)
        for (LeaveType leaveType : LeaveType.values()) {
            try {
                // Check if balance already exists
                if (leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, currentYear).isEmpty()) {
                    LeaveBalance leaveBalance = new LeaveBalance(null, employee, leaveType, currentYear, getAllocationForType(leaveType), 0);
                    leaveBalanceRepository.save(leaveBalance);
                }
            } catch (Exception e) {
                // If constraint violation occurs, it means balance already exists - continue silently
                // This handles race conditions and ensures idempotency
                if (e.getMessage() != null && (e.getMessage().contains("Duplicate") || e.getMessage().contains("constraint"))) {
                    continue; // Balance already exists, skip
                } else {
                    throw e; // Re-throw if it's a different error
                }
            }
        }
    }

    @Override
    public LeaveBalanceResponse getLeaveBalance(Long employeeId, LeaveType leaveType, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, year)
                .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

        return mapToResponse(balance);
    }

    @Override
    public List<LeaveBalanceResponse> getAllLeaveBalances(Long employeeId, int year) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        return leaveBalanceRepository.findByEmployeeIdAndYear(employeeId, year)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUsedDays(Long employeeId, LeaveType leaveType, int usedDays, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, year)
                .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

        if (!balance.hasBalance(usedDays)) {
            throw new BadRequestException(
                    String.format("Insufficient leave balance. Available: %d, Requested: %d",
                            balance.getRemainingDays(), usedDays));
        }

        balance.addUsedDays(usedDays);
        leaveBalanceRepository.save(balance);
    }

    @Override
    @Transactional
    public void revertUsedDays(Long employeeId, LeaveType leaveType, int usedDays, int year) {
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, year)
                .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

        balance.removeUsedDays(usedDays);
        leaveBalanceRepository.save(balance);
    }

    @Override
    public boolean hasBalance(Long employeeId, LeaveType leaveType, int requestedDays, int year) {
        return leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, year)
                .map(balance -> balance.hasBalance(requestedDays))
                .orElse(false);
    }

    // ===== Helper Methods =====

    private int getAllocationForType(LeaveType leaveType) {
        return switch (leaveType) {
            case EARNED_LEAVE -> EARNED_LEAVE_ALLOCATION;
            case CASUAL_LEAVE -> CASUAL_LEAVE_ALLOCATION;
            case SICK_LEAVE -> SICK_LEAVE_ALLOCATION;
            case MATERNITY_LEAVE -> MATERNITY_LEAVE_ALLOCATION;
            case PATERNITY_LEAVE -> PATERNITY_LEAVE_ALLOCATION;
            case UNPAID_LEAVE -> UNPAID_LEAVE_ALLOCATION;
        };
    }

    private LeaveBalanceResponse mapToResponse(LeaveBalance balance) {
        return LeaveBalanceResponse.builder()
                .employeeId(balance.getEmployee().getId())
                .leaveType(balance.getLeaveType())
                .year(balance.getYear())
                .allocatedDays(balance.getAllocatedDays())
                .usedDays(balance.getUsedDays())
                .remainingDays(balance.getRemainingDays())
                .build();
    }
}
