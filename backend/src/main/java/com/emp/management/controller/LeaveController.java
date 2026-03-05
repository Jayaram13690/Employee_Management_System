package com.emp.management.controller;

import com.emp.management.dto.request.LeaveActionRequest;
import com.emp.management.dto.request.LeaveRequestDTO;
import com.emp.management.dto.response.ApiResponse;
import com.emp.management.dto.response.LeaveResponse;
import com.emp.management.service.LeaveService;
import com.emp.management.util.SortFieldValidator;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Leave management REST controller.
 * Employees apply for leave; admins approve/reject.
 */
@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    // ===== Constructor Injection =====

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    /**
     * POST /api/leaves/apply/{employeeId} — Employee applies for leave.
     */
    @PostMapping("/apply/{employeeId}")
    public ResponseEntity<ApiResponse> applyLeave(
            @PathVariable Long employeeId,
            @Valid @RequestBody LeaveRequestDTO request) {

        LeaveResponse response = leaveService.applyLeave(employeeId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Leave applied successfully", response));
    }

    /**
     * PUT /api/leaves/approve/{leaveId} — Admin approves a leave request.
     */
    @PutMapping("/approve/{leaveId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> approveLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveActionRequest actionRequest) {

        LeaveResponse response = leaveService.approveLeave(leaveId, actionRequest);
        return ResponseEntity.ok(ApiResponse.success("Leave approved", response));
    }

    /**
     * PUT /api/leaves/reject/{leaveId} — Admin rejects a leave request.
     */
    @PutMapping("/reject/{leaveId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> rejectLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveActionRequest actionRequest) {

        LeaveResponse response = leaveService.rejectLeave(leaveId, actionRequest);
        return ResponseEntity.ok(ApiResponse.success("Leave rejected", response));
    }

    /**
     * GET /api/leaves/employee/{employeeId} — Get leave history for an employee with pagination.
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse> getLeavesByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = SortFieldValidator.createSort("leave", sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LeaveResponse> responses = leaveService.getLeavesByEmployee(employeeId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Employee leave history", responses));
    }

    /**
     * GET /api/leaves/pending — Get all pending leave requests with pagination (Admin).
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getPendingLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = SortFieldValidator.createSort("leave", sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LeaveResponse> responses = leaveService.getPendingLeaves(pageable);
        return ResponseEntity.ok(ApiResponse.success("Pending leave requests", responses));
    }

    /**
     * GET /api/leaves — Get all leave requests with pagination (Admin).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = SortFieldValidator.createSort("leave", sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LeaveResponse> responses = leaveService.getAllLeaves(pageable);
        return ResponseEntity.ok(ApiResponse.success("All leave requests", responses));
    }

    /**
     * GET /api/leaves/{leaveId} — Get a single leave request detail.
     */
    @GetMapping("/{leaveId}")
    public ResponseEntity<ApiResponse> getLeaveById(@PathVariable Long leaveId) {
        LeaveResponse response = leaveService.getLeaveById(leaveId);
        return ResponseEntity.ok(ApiResponse.success("Leave request details", response));
    }
}
