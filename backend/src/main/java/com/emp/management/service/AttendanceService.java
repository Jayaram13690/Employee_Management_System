package com.emp.management.service;

import com.emp.management.dto.response.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Attendance operations.
 */
public interface AttendanceService {

    AttendanceResponse checkIn(Long employeeId);

    AttendanceResponse checkOut(Long employeeId);

    AttendanceResponse getTodayAttendance(Long employeeId);

    Page<AttendanceResponse> getAttendanceByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<AttendanceResponse> getMonthlyAttendance(Long employeeId, int month, int year, Pageable pageable);

    Page<AttendanceResponse> getAllAttendanceByDate(LocalDate date, Pageable pageable);
}
