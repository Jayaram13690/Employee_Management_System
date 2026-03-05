package com.emp.management.repository;

import com.emp.management.entity.Employee;
import com.emp.management.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Employee entity with search and filtering.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserId(Long userId);

    List<Employee> findByDepartmentId(Long departmentId);

    /**
     * Get all employees excluding ADMIN role users.
     */
    @Query("SELECT e FROM Employee e JOIN e.user u WHERE u.role <> :role")
    Page<Employee> findAllExcludingRole(@Param("role") Role role, Pageable pageable);

    /**
     * Search employees by name, email, designation, or phone — excluding ADMIN
     * role.
     */
    @Query("SELECT e FROM Employee e JOIN e.user u WHERE u.role <> :role AND (" +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.designation) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.phone) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Employee> searchEmployeesExcludingRole(@Param("search") String search, @Param("role") Role role,
            Pageable pageable);

    long countByActiveTrue();

    long countByActiveFalse();

    boolean existsByPhone(String phone);
}
