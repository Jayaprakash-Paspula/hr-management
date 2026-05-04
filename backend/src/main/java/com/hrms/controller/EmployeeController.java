package com.hrms.controller;

import com.hrms.dto.EmployeeDto;
import com.hrms.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Employee Controller
 */
@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Management", description = "Employee CRUD operations")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Get all employees
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get all employees", description = "Retrieve paginated list of all active employees")
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(Pageable pageable) {
        log.info("Fetching all employees");
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    /**
     * Get employee by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get employee by ID", description = "Retrieve employee details by ID")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable UUID id) {
        log.info("Fetching employee by id: {}", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /**
     * Create new employee
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Create employee", description = "Create a new employee record")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        log.info("Creating new employee");
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeDto));
    }

    /**
     * Update employee
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Update employee", description = "Update an existing employee record")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable UUID id, @Valid @RequestBody EmployeeDto employeeDto) {
        log.info("Updating employee: {}", id);
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDto));
    }

    /**
     * Delete employee (soft delete)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete employee", description = "Delete (deactivate) an employee record")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        log.info("Deleting employee: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search employees
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Search employees", description = "Search employees by name or email")
    public ResponseEntity<Page<EmployeeDto>> searchEmployees(
            @RequestParam String search,
            Pageable pageable) {
        log.info("Searching employees with query: {}", search);
        return ResponseEntity.ok(employeeService.searchEmployees(search, pageable));
    }

    /**
     * Get employees by department
     */
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get employees by department", description = "Retrieve employees in a specific department")
    public ResponseEntity<Page<EmployeeDto>> getEmployeesByDepartment(
            @PathVariable UUID departmentId,
            Pageable pageable) {
        log.info("Fetching employees by department: {}", departmentId);
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId, pageable));
    }

    /**
     * Get direct reports
     */
    @GetMapping("/manager/{managerId}/reports")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get direct reports", description = "Retrieve direct reports of a manager")
    public ResponseEntity<Page<EmployeeDto>> getDirectReports(
            @PathVariable UUID managerId,
            Pageable pageable) {
        log.info("Fetching direct reports for manager: {}", managerId);
        return ResponseEntity.ok(employeeService.getDirectReports(managerId, pageable));
    }
}

