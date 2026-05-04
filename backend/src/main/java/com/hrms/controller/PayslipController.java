package com.hrms.controller;

import com.hrms.dto.PayslipDto;
import com.hrms.service.PayslipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

/**
 * Payslip Controller
 */
@RestController
@RequestMapping("/payslips")
@Tag(name = "Payslip Management", description = "Payslip generation and retrieval")
@Slf4j
public class PayslipController {

    @Autowired
    private PayslipService payslipService;

    /**
     * Generate payslip for employee
     */
    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Generate payslip", description = "Generate payslip for an employee for a specific month")
    public ResponseEntity<PayslipDto> generatePayslip(
            @RequestParam UUID employeeId,
            @RequestParam String yearMonth) {
        log.info("Generating payslip for employee: {} for month: {}", employeeId, yearMonth);
        PayslipDto payslipDto = payslipService.generatePayslip(employeeId, YearMonth.parse(yearMonth));
        return ResponseEntity.status(HttpStatus.CREATED).body(payslipDto);
    }

    /**
     * Get payslip by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN', 'EMPLOYEE')")
    @Operation(summary = "Get payslip", description = "Retrieve payslip details by ID")
    public ResponseEntity<PayslipDto> getPayslipById(@PathVariable UUID id) {
        log.info("Fetching payslip: {}", id);
        return ResponseEntity.ok(payslipService.getPayslipById(id));
    }

    /**
     * Get all payslips for an employee
     */
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN', 'EMPLOYEE')")
    @Operation(summary = "Get employee payslips", description = "Retrieve all payslips for a specific employee")
    public ResponseEntity<Page<PayslipDto>> getPayslipsByEmployee(
            @PathVariable UUID employeeId,
            Pageable pageable) {
        log.info("Fetching payslips for employee: {}", employeeId);
        return ResponseEntity.ok(payslipService.getPayslipsByEmployee(employeeId, pageable));
    }

    /**
     * Get all payslips for a specific month
     */
    @GetMapping("/month/{payrollMonth}")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Get payslips by month", description = "Retrieve all payslips generated for a specific month")
    public ResponseEntity<Page<PayslipDto>> getPayslipsByMonth(
            @PathVariable LocalDate payrollMonth,
            Pageable pageable) {
        log.info("Fetching payslips for month: {}", payrollMonth);
        return ResponseEntity.ok(payslipService.getPayslipsByMonth(payrollMonth, pageable));
    }
}

