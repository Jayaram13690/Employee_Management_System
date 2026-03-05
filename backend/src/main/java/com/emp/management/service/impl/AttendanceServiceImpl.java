package com.emp.management.service.impl;

import com.emp.management.dto.response.AttendanceResponse;
import com.emp.management.entity.Attendance;
import com.emp.management.entity.Employee;
import com.emp.management.enums.AttendanceStatus;
import com.emp.management.exception.BadRequestException;
import com.emp.management.exception.ResourceNotFoundException;
import com.emp.management.repository.AttendanceRepository;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Attendance service implementation — handles check-in/check-out,
 * automatic status calculation, and attendance reports.
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    // ===== Constructor Injection =====

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Business rules for attendance status
    private static final LocalTime STANDARD_START = LocalTime.of(9, 0); // 9:00 AM
    private static final LocalTime LATE_THRESHOLD = LocalTime.of(9, 30); // 9:30 AM
    private static final LocalTime HALF_DAY_THRESHOLD = LocalTime.of(13, 0); // 1:00 PM

    @Override
    @Transactional
    public AttendanceResponse checkIn(Long employeeId) {
        Employee employee = findEmployeeOrThrow(employeeId);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Check if already checked in today
        Optional<Attendance> existing = attendanceRepository.findByEmployeeIdAndDate(employeeId, today);
        if (existing.isPresent()) {
            throw new BadRequestException("Already checked in today");
        }

        // Calculate attendance status based on check-in time
        AttendanceStatus status = calculateStatus(now);

        Attendance attendance = new Attendance(null, employee, today, now, null, status, null);

        attendance = attendanceRepository.save(attendance);
        System.out.println("Employee " + employee.getFullName() + " checked in at " + now + " — Status: " + status);

        return mapToResponse(attendance);
    }

    @Override
    @Transactional
    public AttendanceResponse checkOut(Long employeeId) {
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(
                        () -> new BadRequestException("No check-in record found for today. Please check in first."));

        if (attendance.getCheckOut() != null) {
            throw new BadRequestException("Already checked out today");
        }

        attendance.setCheckOut(LocalTime.now());
        attendance = attendanceRepository.save(attendance);

        System.out.println("Employee ID " + employeeId + " checked out at " + attendance.getCheckOut());
        return mapToResponse(attendance);
    }

    @Override
    public AttendanceResponse getTodayAttendance(Long employeeId) {
        findEmployeeOrThrow(employeeId);
        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
                .orElse(null);

        if (attendance == null) {
            // Return an absent placeholder for today
            return AttendanceResponse.builder()
                    .employeeId(employeeId)
                    .date(today)
                    .status(AttendanceStatus.ABSENT)
                    .build();
        }

        return mapToResponse(attendance);
    }

    @Override
    public Page<AttendanceResponse> getAttendanceByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        findEmployeeOrThrow(employeeId);
        List<AttendanceResponse> content = attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return new org.springframework.data.domain.PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<AttendanceResponse> getMonthlyAttendance(Long employeeId, int month, int year, Pageable pageable) {
        findEmployeeOrThrow(employeeId);
        List<AttendanceResponse> content = attendanceRepository.findMonthlyAttendance(employeeId, month, year)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return new org.springframework.data.domain.PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<AttendanceResponse> getAllAttendanceByDate(LocalDate date, Pageable pageable) {
        List<AttendanceResponse> content = attendanceRepository.findByDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return new org.springframework.data.domain.PageImpl<>(content, pageable, content.size());
    }

    // ===== Helper Methods =====

    /**
     * Calculate attendance status based on check-in time.
     * Before 9:00 AM → PRESENT
     * 9:00 - 9:30 AM → LATE
     * After 1:00 PM → HALF_DAY
     */
    private AttendanceStatus calculateStatus(LocalTime checkInTime) {
        if (checkInTime.isBefore(STANDARD_START) || checkInTime.equals(STANDARD_START)) {
            return AttendanceStatus.PRESENT;
        } else if (checkInTime.isBefore(LATE_THRESHOLD) || checkInTime.equals(LATE_THRESHOLD)) {
            return AttendanceStatus.LATE;
        } else if (checkInTime.isAfter(HALF_DAY_THRESHOLD)) {
            return AttendanceStatus.HALF_DAY;
        }
        return AttendanceStatus.LATE;
    }

    private Employee findEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(attendance.getEmployee().getFullName())
                .date(attendance.getDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .status(attendance.getStatus())
                .build();
    }
}
