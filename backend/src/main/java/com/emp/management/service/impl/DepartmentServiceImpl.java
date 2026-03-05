package com.emp.management.service.impl;

import com.emp.management.dto.request.DepartmentRequest;
import com.emp.management.dto.response.DepartmentResponse;
import com.emp.management.dto.response.EmployeeResponse;
import com.emp.management.entity.Department;
import com.emp.management.entity.Employee;
import com.emp.management.exception.BadRequestException;
import com.emp.management.exception.ResourceNotFoundException;
import com.emp.management.repository.DepartmentRepository;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Department service implementation — handles CRUD and employee-department
 * relationships.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    // ===== Constructor Injection =====

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        // Check for duplicate name
        if (departmentRepository.existsByName(request.getName())) {
            throw new BadRequestException("Department already exists: " + request.getName());
        }

        Department department = new Department(null, request.getName(), request.getDescription(), new ArrayList<>(), null, null);

        department = departmentRepository.save(department);
        System.out.println("Created department: " + department.getName() + " (ID: " + department.getId() + ")");

        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = findDepartmentOrThrow(id);
        return mapToResponse(department);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = findDepartmentOrThrow(id);

        // Check for duplicate name (exclude current department)
        departmentRepository.findByName(request.getName())
                .filter(d -> !d.getId().equals(id))
                .ifPresent(d -> {
                    throw new BadRequestException("Department name already exists: " + request.getName());
                });

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department = departmentRepository.save(department);

        System.out.println("Updated department: " + department.getName() + " (ID: " + department.getId() + ")");
        return mapToResponse(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = findDepartmentOrThrow(id);

        // Unassign all employees from this department before deleting
        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        for (Employee emp : employees) {
            emp.setDepartment(null);
            employeeRepository.save(emp);
        }

        departmentRepository.delete(department);
        System.out.println("Deleted department: " + department.getName() + " (ID: " + id + ")");
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(Long departmentId) {
        // Validate department exists
        findDepartmentOrThrow(departmentId);

        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapEmployeeToResponse)
                .collect(Collectors.toList());
    }

    // ===== Helper Methods =====

    private Department findDepartmentOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }

    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .employeeCount(department.getEmployees() != null ? department.getEmployees().size() : 0)
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    private EmployeeResponse mapEmployeeToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getUser().getEmail())
                .phone(employee.getPhone())
                .designation(employee.getDesignation())
                .dateOfJoining(employee.getDateOfJoining())
                .salary(employee.getSalary())
                .active(employee.isActive())
                .profilePhoto(employee.getProfilePhoto())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
