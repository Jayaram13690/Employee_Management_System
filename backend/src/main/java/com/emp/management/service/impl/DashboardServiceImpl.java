package com.emp.management.service.impl;

import com.emp.management.dto.response.DashboardResponse;
import com.emp.management.dto.response.EmployeeDashboardResponse;
import com.emp.management.dto.response.LeaveResponse;
import com.emp.management.entity.Attendance;
import com.emp.management.entity.LeaveRequest;
import com.emp.management.enums.AttendanceStatus;
import com.emp.management.enums.LeaveStatus;
import com.emp.management.exception.ResourceNotFoundException;
import com.emp.management.repository.AttendanceRepository;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.repository.LeaveRequestRepository;
import com.emp.management.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard service — aggregates summary data for admin and employee views.
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    // ===== Constructor Injection =====

    public DashboardServiceImpl(EmployeeRepository employeeRepository, AttendanceRepository attendanceRepository, LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public DashboardResponse getAdminDashboard() {
        LocalDate today = LocalDate.now();

        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByActiveTrue();
        long inactiveEmployees = employeeRepository.countByActiveFalse();

        long todayPresent = attendanceRepository.countByDateAndStatus(today, AttendanceStatus.PRESENT)
                + attendanceRepository.countByDateAndStatus(today, AttendanceStatus.LATE);
        long todayAbsent = activeEmployees - attendanceRepository.countByDate(today);
        long todayOnLeave = attendanceRepository.countByDateAndStatus(today, AttendanceStatus.ON_LEAVE);

        long pendingLeaves = leaveRequestRepository.countByStatus(LeaveStatus.PENDING);

        return DashboardResponse.builder()
                .totalEmployees(totalEmployees)
                .activeEmployees(activeEmployees)
                .inactiveEmployees(inactiveEmployees)
                .todayPresent(todayPresent)
                .todayAbsent(Math.max(0, todayAbsent))
                .todayOnLeave(todayOnLeave)
                .pendingLeaveRequests(pendingLeaves)
                .build();
    }

    @Override
    public EmployeeDashboardResponse getEmployeeDashboard(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        // Attendance summary for the current month
        LocalDate now = LocalDate.now();
        List<Attendance> monthlyAttendance = attendanceRepository.findMonthlyAttendance(
                employeeId, now.getMonthValue(), now.getYear());

        long totalPresent = monthlyAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        long totalAbsent = monthlyAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        long totalLate = monthlyAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
        long totalHalfDay = monthlyAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.HALF_DAY).count();

        // Leave summary
        List<LeaveRequest> allLeaves = leaveRequestRepository
                .findByEmployeeIdOrderByCreatedAtDesc(employeeId);

        long totalLeavesApplied = allLeaves.size();
        long pendingLeaves = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING).count();
        long approvedLeaves = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED).count();
        long rejectedLeaves = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.REJECTED).count();

        // Recent 5 leaves
        List<LeaveResponse> recentLeaves = allLeaves.stream()
                .limit(5)
                .map(this::mapLeaveToResponse)
                .collect(Collectors.toList());

        return EmployeeDashboardResponse.builder()
                .totalPresent(totalPresent)
                .totalAbsent(totalAbsent)
                .totalLate(totalLate)
                .totalHalfDay(totalHalfDay)
                .totalLeavesApplied(totalLeavesApplied)
                .pendingLeaves(pendingLeaves)
                .approvedLeaves(approvedLeaves)
                .rejectedLeaves(rejectedLeaves)
                .recentLeaves(recentLeaves)
                .build();
    }

    private LeaveResponse mapLeaveToResponse(LeaveRequest leave) {
        return LeaveResponse.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployee().getId())
                .employeeName(leave.getEmployee().getFullName())
                .leaveType(leave.getLeaveType())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .leaveDays(leave.getLeaveDays())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .adminRemarks(leave.getAdminRemarks())
                .createdAt(leave.getCreatedAt())
                .updatedAt(leave.getUpdatedAt())
                .build();
    }
}
