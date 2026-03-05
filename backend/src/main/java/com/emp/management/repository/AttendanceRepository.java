package com.emp.management.repository;

import com.emp.management.entity.Attendance;
import com.emp.management.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Attendance entity with date-based filtering.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    List<Attendance> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByDate(LocalDate date);

    long countByDateAndStatus(LocalDate date, AttendanceStatus status);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.date = :date")
    long countByDate(@Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.employee.id = :employeeId " +
            "AND MONTH(a.date) = :month AND YEAR(a.date) = :year")
    List<Attendance> findMonthlyAttendance(@Param("employeeId") Long employeeId,
            @Param("month") int month,
            @Param("year") int year);
}
