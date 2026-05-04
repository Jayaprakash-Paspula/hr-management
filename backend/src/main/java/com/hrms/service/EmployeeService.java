package com.hrms.service;

import com.hrms.dto.EmployeeDto;
import com.hrms.entity.Department;
import com.hrms.entity.Employee;
import com.hrms.entity.User;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Employee Service
 */
@Service
@Slf4j
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Get all employees with pagination
     */
    public Page<EmployeeDto> getAllEmployees(Pageable pageable) {
        return employeeRepository.findByActiveTrue(pageable).map(this::convertToDto);
    }

    /**
     * Get employee by ID
     */
    public EmployeeDto getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return convertToDto(employee);
    }

    /**
     * Create new employee
     */
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        // Check if user exists
        User user = userRepository.findByEmail(employeeDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + employeeDto.getEmail()));

        // Check if department exists
        Department department = departmentRepository.findById(UUID.fromString(employeeDto.getDepartmentId()))
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        employee.setDepartment(department);
        employee.setDesignation(employeeDto.getDesignation());
        employee.setJoiningDate(LocalDate.parse(employeeDto.getJoiningDate(), DATE_FORMATTER));
        employee.setBaseSalary(new BigDecimal(employeeDto.getBaseSalary()));
        employee.setHourlyRate(employeeDto.getHourlyRate() != null ? new BigDecimal(employeeDto.getHourlyRate()) : null);
        employee.setOvertimeRate(employeeDto.getOvertimeRate() != null ? new BigDecimal(employeeDto.getOvertimeRate()) : null);
        employee.setActive(true);

        if (employeeDto.getManagerId() != null && !employeeDto.getManagerId().isEmpty()) {
            Employee manager = employeeRepository.findById(UUID.fromString(employeeDto.getManagerId()))
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            employee.setManager(manager);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created: {}", savedEmployee.getId());
        return convertToDto(savedEmployee);
    }

    /**
     * Update employee
     */
    public EmployeeDto updateEmployee(UUID id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setPhone(employeeDto.getPhone());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setBaseSalary(new BigDecimal(employeeDto.getBaseSalary()));
        employee.setHourlyRate(employeeDto.getHourlyRate() != null ? new BigDecimal(employeeDto.getHourlyRate()) : null);
        employee.setOvertimeRate(employeeDto.getOvertimeRate() != null ? new BigDecimal(employeeDto.getOvertimeRate()) : null);

        if (employeeDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(UUID.fromString(employeeDto.getDepartmentId()))
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }

        if (employeeDto.getManagerId() != null && !employeeDto.getManagerId().isEmpty()) {
            Employee manager = employeeRepository.findById(UUID.fromString(employeeDto.getManagerId()))
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            employee.setManager(manager);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated: {}", id);
        return convertToDto(updatedEmployee);
    }

    /**
     * Delete employee (soft delete)
     */
    public void deleteEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employee.setActive(false);
        employeeRepository.save(employee);
        log.info("Employee deleted (soft): {}", id);
    }

    /**
     * Search employees by name or email
     */
    public Page<EmployeeDto> searchEmployees(String search, Pageable pageable) {
        return employeeRepository.searchByNameOrEmail(search, pageable).map(this::convertToDto);
    }

    /**
     * Get employees by department
     */
    public Page<EmployeeDto> getEmployeesByDepartment(UUID departmentId, Pageable pageable) {
        return employeeRepository.findByDepartmentId(departmentId, pageable).map(this::convertToDto);
    }

    /**
     * Get direct reports of a manager
     */
    public Page<EmployeeDto> getDirectReports(UUID managerId, Pageable pageable) {
        return employeeRepository.findByManagerId(managerId, pageable).map(this::convertToDto);
    }

    /**
     * Convert Employee entity to DTO
     */
    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId().toString());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartmentId(employee.getDepartment().getId().toString());
        dto.setDepartmentName(employee.getDepartment().getName());
        dto.setDesignation(employee.getDesignation());
        dto.setJoiningDate(employee.getJoiningDate().toString());
        dto.setBaseSalary(employee.getBaseSalary().toString());
        dto.setHourlyRate(employee.getHourlyRate() != null ? employee.getHourlyRate().toString() : null);
        dto.setOvertimeRate(employee.getOvertimeRate() != null ? employee.getOvertimeRate().toString() : null);
        if (employee.getManager() != null) {
            dto.setManagerId(employee.getManager().getId().toString());
            dto.setManagerName(employee.getManager().getFullName());
        }
        dto.setActive(employee.isActive());
        dto.setCreatedAt(employee.getCreatedAt().toString());
        dto.setUpdatedAt(employee.getUpdatedAt().toString());
        return dto;
    }
}

