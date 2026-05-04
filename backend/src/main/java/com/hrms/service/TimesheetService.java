package com.hrms.service;

import com.hrms.dto.TimesheetDto;
import com.hrms.entity.Employee;
import com.hrms.entity.Timesheet;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.TimesheetRepository;
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
 * Timesheet Service
 */
@Service
@Slf4j
@Transactional
public class TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Get all timesheets for an employee
     */
    public Page<TimesheetDto> getTimesheetsByEmployee(UUID employeeId, Pageable pageable) {
        return timesheetRepository.findByEmployeeId(employeeId, pageable).map(this::convertToDto);
    }

    /**
     * Get timesheet by ID
     */
    public TimesheetDto getTimesheetById(UUID id) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + id));
        return convertToDto(timesheet);
    }

    /**
     * Create new timesheet entry
     */
    public TimesheetDto createTimesheet(TimesheetDto timesheetDto) {
        Employee employee = employeeRepository.findById(UUID.fromString(timesheetDto.getEmployeeId()))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate timesheetDate = LocalDate.parse(timesheetDto.getTimesheetDate(), DATE_FORMATTER);

        // Check if timesheet already exists for this employee and date
        if (timesheetRepository.findByEmployeeIdAndTimesheetDate(employee.getId(), timesheetDate).isPresent()) {
            throw new RuntimeException("Timesheet already exists for this date");
        }

        Timesheet timesheet = new Timesheet();
        timesheet.setEmployee(employee);
        timesheet.setTimesheetDate(timesheetDate);
        timesheet.setHoursWorked(new BigDecimal(timesheetDto.getHoursWorked()));
        timesheet.setProject(timesheetDto.getProject());
        timesheet.setTaskDescription(timesheetDto.getTaskDescription());
        timesheet.setStatus("DRAFT");

        Timesheet savedTimesheet = timesheetRepository.save(timesheet);
        log.info("Timesheet created: {}", savedTimesheet.getId());
        return convertToDto(savedTimesheet);
    }

    /**
     * Update timesheet
     */
    public TimesheetDto updateTimesheet(UUID id, TimesheetDto timesheetDto) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + id));

        if (!"DRAFT".equals(timesheet.getStatus())) {
            throw new RuntimeException("Can only update DRAFT timesheets");
        }

        timesheet.setHoursWorked(new BigDecimal(timesheetDto.getHoursWorked()));
        timesheet.setProject(timesheetDto.getProject());
        timesheet.setTaskDescription(timesheetDto.getTaskDescription());

        Timesheet updatedTimesheet = timesheetRepository.save(timesheet);
        log.info("Timesheet updated: {}", id);
        return convertToDto(updatedTimesheet);
    }

    /**
     * Submit timesheet for approval
     */
    public TimesheetDto submitTimesheet(UUID id) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found with id: " + id));

        if (!"DRAFT".equals(timesheet.getStatus())) {
            throw new RuntimeException("Can only submit DRAFT timesheets");
        }

        timesheet.setStatus("SUBMITTED");
        timesheet.setSubmittedAt(java.time.LocalDateTime.now());

        Timesheet updatedTimesheet = timesheetRepository.save(timesheet);
        log.info("Timesheet submitted: {}", id);
        return convertToDto(updatedTimesheet);
    }

    /**
     * Get pending approvals for manager
     */
    public Page<TimesheetDto> getPendingApprovals(UUID managerId, Pageable pageable) {
        return timesheetRepository.findPendingApprovalsByManager(managerId, pageable).map(this::convertToDto);
    }

    /**
     * Get timesheets for date range
     */
    public Page<TimesheetDto> getTimesheetsByDateRange(UUID employeeId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return timesheetRepository.findByEmployeeAndDateRange(employeeId, fromDate, toDate, pageable).map(this::convertToDto);
    }

    /**
     * Convert Timesheet entity to DTO
     */
    private TimesheetDto convertToDto(Timesheet timesheet) {
        TimesheetDto dto = new TimesheetDto();
        dto.setId(timesheet.getId().toString());
        dto.setEmployeeId(timesheet.getEmployee().getId().toString());
        dto.setEmployeeName(timesheet.getEmployee().getFullName());
        dto.setTimesheetDate(timesheet.getTimesheetDate().toString());
        dto.setHoursWorked(timesheet.getHoursWorked().toString());
        dto.setProject(timesheet.getProject());
        dto.setTaskDescription(timesheet.getTaskDescription());
        dto.setStatus(timesheet.getStatus());
        if (timesheet.getSubmittedAt() != null) {
            dto.setSubmittedAt(timesheet.getSubmittedAt().toString());
        }
        dto.setCreatedAt(timesheet.getCreatedAt().toString());
        dto.setUpdatedAt(timesheet.getUpdatedAt().toString());
        return dto;
    }
}

