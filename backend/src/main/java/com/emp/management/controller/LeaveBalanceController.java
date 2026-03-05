package com.emp.management.controller;

import com.emp.management.dto.response.ApiResponse;
import com.emp.management.dto.response.LeaveBalanceResponse;
import com.emp.management.enums.LeaveType;
import com.emp.management.service.LeaveBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

/**
 * Leave balance REST controller.
 * Employees can view their leave balance information.
 */
@RestController
@RequestMapping("/api/leaves/balance")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    // ===== Constructor Injection =====

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    /**
     * GET /api/leaves/balance/{employeeId} — Get all leave balances for current year.
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse> getLeaveBalances(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year) {

        int targetYear = year != null ? year : Year.now().getValue();
        List<LeaveBalanceResponse> responses = leaveBalanceService.getAllLeaveBalances(employeeId, targetYear);

        return ResponseEntity.ok(ApiResponse.success("Leave balances retrieved", responses));
    }

    /**
     * GET /api/leaves/balance/{employeeId}/{leaveType} — Get specific leave type balance.
     */
    @GetMapping("/{employeeId}/{leaveType}")
    public ResponseEntity<ApiResponse> getLeaveBalance(
            @PathVariable Long employeeId,
            @PathVariable LeaveType leaveType,
            @RequestParam(required = false) Integer year) {

        int targetYear = year != null ? year : Year.now().getValue();
        LeaveBalanceResponse response = leaveBalanceService.getLeaveBalance(employeeId, leaveType, targetYear);

        return ResponseEntity.ok(ApiResponse.success("Leave balance retrieved", response));
    }
}
