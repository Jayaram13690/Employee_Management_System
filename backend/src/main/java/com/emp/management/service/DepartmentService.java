package com.emp.management.service;

import com.emp.management.dto.request.DepartmentRequest;
import com.emp.management.dto.response.DepartmentResponse;
import com.emp.management.dto.response.EmployeeResponse;

import java.util.List;

/**
 * Service interface for Department CRUD operations.
 */
public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse getDepartmentById(Long id);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    void deleteDepartment(Long id);

    List<EmployeeResponse> getEmployeesByDepartment(Long departmentId);
}
