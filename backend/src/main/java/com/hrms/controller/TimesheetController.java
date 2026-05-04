package com.hrms.controller;

import com.hrms.dto.TimesheetDto;
import com.hrms.service.TimesheetService;
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

import java.time.LocalDate;
import java.util.UUID;

/**
 * Timesheet Controller
 */
@RestController
@RequestMapping("/timesheets")
@Tag(name = "Timesheet Management", description = "Timesheet CRUD operations")
@Slf4j
public class TimesheetController {

    @Autowired
    private TimesheetService timesheetService;

    /**
     * Get timesheets for an employee
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get employee timesheets", description = "Retrieve timesheets for a specific employee")
    public ResponseEntity<Page<TimesheetDto>> getTimesheetsByEmployee(
            @PathVariable UUID employeeId,
            Pageable pageable) {
        log.info("Fetching timesheets for employee: {}", employeeId);
        return ResponseEntity.ok(timesheetService.getTimesheetsByEmployee(employeeId, pageable));
    }

    /**
     * Get timesheet by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get timesheet by ID", description = "Retrieve timesheet details")
    public ResponseEntity<TimesheetDto> getTimesheetById(@PathVariable UUID id) {
        log.info("Fetching timesheet: {}", id);
        return ResponseEntity.ok(timesheetService.getTimesheetById(id));
    }

    /**
     * Create new timesheet entry
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Create timesheet", description = "Create a new timesheet entry")
    public ResponseEntity<TimesheetDto> createTimesheet(@Valid @RequestBody TimesheetDto timesheetDto) {
        log.info("Creating new timesheet entry");
        return ResponseEntity.status(HttpStatus.CREATED).body(timesheetService.createTimesheet(timesheetDto));
    }

    /**
     * Update timesheet
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Update timesheet", description = "Update a timesheet entry (only DRAFT status)")
    public ResponseEntity<TimesheetDto> updateTimesheet(
            @PathVariable UUID id,
            @Valid @RequestBody TimesheetDto timesheetDto) {
        log.info("Updating timesheet: {}", id);
        return ResponseEntity.ok(timesheetService.updateTimesheet(id, timesheetDto));
    }

    /**
     * Submit timesheet for approval
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Submit timesheet", description = "Submit a timesheet for manager approval")
    public ResponseEntity<TimesheetDto> submitTimesheet(@PathVariable UUID id) {
        log.info("Submitting timesheet: {}", id);
        return ResponseEntity.ok(timesheetService.submitTimesheet(id));
    }

    /**
     * Get pending approvals for manager
     */
    @GetMapping("/approvals/pending")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get pending approvals", description = "Get timesheets pending approval for manager")
    public ResponseEntity<Page<TimesheetDto>> getPendingApprovals(
            @RequestParam UUID managerId,
            Pageable pageable) {
        log.info("Fetching pending approvals for manager: {}", managerId);
        return ResponseEntity.ok(timesheetService.getPendingApprovals(managerId, pageable));
    }

    /**
     * Get timesheets for date range
     */
    @GetMapping("/employee/{employeeId}/date-range")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get timesheets by date range", description = "Get timesheets for employee within date range")
    public ResponseEntity<Page<TimesheetDto>> getTimesheetsByDateRange(
            @PathVariable UUID employeeId,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            Pageable pageable) {
        log.info("Fetching timesheets for employee: {} from {} to {}", employeeId, fromDate, toDate);
        return ResponseEntity.ok(timesheetService.getTimesheetsByDateRange(employeeId, fromDate, toDate, pageable));
    }
}

