package com.emp.management.controller;

import com.emp.management.dto.response.ApiResponse;
import com.emp.management.dto.response.AttendanceResponse;
import com.emp.management.service.AttendanceService;
import com.emp.management.util.SortFieldValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Attendance management REST controller.
 * Employees check in/out; admins can view all attendance.
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // ===== Constructor Injection =====

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * POST /api/attendance/check-in/{employeeId} — Employee checks in.
     */
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<ApiResponse> checkIn(@PathVariable Long employeeId) {
        AttendanceResponse response = attendanceService.checkIn(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Checked in successfully", response));
    }

    /**
     * POST /api/attendance/check-out/{employeeId} — Employee checks out.
     */
    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<ApiResponse> checkOut(@PathVariable Long employeeId) {
        AttendanceResponse response = attendanceService.checkOut(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Checked out successfully", response));
    }

    /**
     * GET /api/attendance/today/{employeeId} — Get today's attendance for an
     * employee.
     */
    @GetMapping("/today/{employeeId}")
    public ResponseEntity<ApiResponse> getTodayAttendance(@PathVariable Long employeeId) {
        AttendanceResponse response = attendanceService.getTodayAttendance(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Today's attendance", response));
    }

    /**
     * GET /api/attendance/range/{employeeId} — Get attendance by date range with pagination.
     */
    @GetMapping("/range/{employeeId}")
    public ResponseEntity<ApiResponse> getAttendanceByDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = SortFieldValidator.createSort("attendance", sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AttendanceResponse> responses = attendanceService.getAttendanceByDateRange(employeeId, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success("Attendance by date range", responses));
    }

    /**
     * GET /api/attendance/monthly/{employeeId} — Get monthly attendance report with pagination.
     */
    @GetMapping("/monthly/{employeeId}")
    public ResponseEntity<ApiResponse> getMonthlyAttendance(
            @PathVariable Long employeeId,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = SortFieldValidator.createSort("attendance", sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AttendanceResponse> responses = attendanceService.getMonthlyAttendance(employeeId, month, year, pageable);
        return ResponseEntity.ok(ApiResponse.success("Monthly attendance report", responses));
    }

    /**
     * GET /api/attendance/date — Get all employees' attendance for a given date
     * with pagination (Admin).
     */
    @GetMapping("/date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAttendanceByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = SortFieldValidator.createSort("attendance", sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AttendanceResponse> responses = attendanceService.getAllAttendanceByDate(date, pageable);
        return ResponseEntity.ok(ApiResponse.success("Attendance for date", responses));
    }
}
