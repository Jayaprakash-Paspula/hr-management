package com.hrms.service;

import com.hrms.dto.PayslipDto;
import com.hrms.entity.Employee;
import com.hrms.entity.Payslip;
import com.hrms.entity.PayslipDetail;
import com.hrms.entity.Timesheet;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.PayslipRepository;
import com.hrms.repository.TimesheetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

/**
 * Payslip Service
 */
@Service
@Slf4j
@Transactional
public class PayslipService {

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private CompensationCalculatorService compensationCalculatorService;

    /**
     * Generate payslip for an employee for a specific month
     */
    public PayslipDto generatePayslip(UUID employeeId, YearMonth yearMonth) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Check if payslip already exists for this month
        LocalDate payrollMonth = yearMonth.atDay(1);
        if (payslipRepository.findByEmployeeIdAndPayrollMonth(employeeId, payrollMonth).isPresent()) {
            throw new RuntimeException("Payslip already generated for this month");
        }

        // Get timesheets for the month
        LocalDate fromDate = yearMonth.atDay(1);
        LocalDate toDate = yearMonth.atEndOfMonth();

        // Calculate total hours worked in the month
        BigDecimal totalHoursWorked = BigDecimal.ZERO;

        // For simplicity, we'll use the monthly salary as base pay
        BigDecimal basePay = employee.getBaseSalary();
        BigDecimal overtimePay = BigDecimal.ZERO;
        BigDecimal bonus = BigDecimal.ZERO;

        // Calculate tax (simplified: 10% of gross)
        BigDecimal grossPay = basePay.add(overtimePay).add(bonus);
        BigDecimal taxDeduction = grossPay.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);

        // Other deductions (can be customized)
        BigDecimal otherDeductions = BigDecimal.ZERO;
        BigDecimal totalDeductions = taxDeduction.add(otherDeductions);

        // Net pay
        BigDecimal netPay = grossPay.subtract(totalDeductions);

        // Create payslip
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setPayrollMonth(payrollMonth);
        payslip.setBasePay(basePay);
        payslip.setOvertimePay(overtimePay);
        payslip.setBonus(bonus);
        payslip.setTotalEarnings(grossPay);
        payslip.setTaxDeduction(taxDeduction);
        payslip.setOtherDeductions(otherDeductions);
        payslip.setTotalDeductions(totalDeductions);
        payslip.setNetPay(netPay);

        // Add details
        PayslipDetail baseSalaryDetail = new PayslipDetail();
        baseSalaryDetail.setPayslip(payslip);
        baseSalaryDetail.setDescription("Base Salary");
        baseSalaryDetail.setAmount(basePay);
        baseSalaryDetail.setCategory("EARNINGS");
        payslip.getDetails().add(baseSalaryDetail);

        if (overtimePay.compareTo(BigDecimal.ZERO) > 0) {
            PayslipDetail overtimeDetail = new PayslipDetail();
            overtimeDetail.setPayslip(payslip);
            overtimeDetail.setDescription("Overtime Pay");
            overtimeDetail.setAmount(overtimePay);
            overtimeDetail.setCategory("EARNINGS");
            payslip.getDetails().add(overtimeDetail);
        }

        if (bonus.compareTo(BigDecimal.ZERO) > 0) {
            PayslipDetail bonusDetail = new PayslipDetail();
            bonusDetail.setPayslip(payslip);
            bonusDetail.setDescription("Bonus");
            bonusDetail.setAmount(bonus);
            bonusDetail.setCategory("EARNINGS");
            payslip.getDetails().add(bonusDetail);
        }

        PayslipDetail taxDetail = new PayslipDetail();
        taxDetail.setPayslip(payslip);
        taxDetail.setDescription("Tax Deduction");
        taxDetail.setAmount(taxDeduction);
        taxDetail.setCategory("DEDUCTION");
        payslip.getDetails().add(taxDetail);

        Payslip savedPayslip = payslipRepository.save(payslip);
        log.info("Payslip generated for employee: {} for month: {}", employeeId, yearMonth);
        return convertToDto(savedPayslip);
    }

    /**
     * Get payslip by ID
     */
    public PayslipDto getPayslipById(UUID id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + id));
        return convertToDto(payslip);
    }

    /**
     * Get all payslips for an employee
     */
    public Page<PayslipDto> getPayslipsByEmployee(UUID employeeId, Pageable pageable) {
        return payslipRepository.findByEmployeeId(employeeId, pageable).map(this::convertToDto);
    }

    /**
     * Get all payslips for a specific month
     */
    public Page<PayslipDto> getPayslipsByMonth(LocalDate payrollMonth, Pageable pageable) {
        return payslipRepository.findByPayrollMonth(payrollMonth, pageable).map(this::convertToDto);
    }

    /**
     * Convert Payslip entity to DTO
     */
    private PayslipDto convertToDto(Payslip payslip) {
        PayslipDto dto = new PayslipDto();
        dto.setId(payslip.getId().toString());
        dto.setEmployeeId(payslip.getEmployee().getId().toString());
        dto.setEmployeeName(payslip.getEmployee().getFullName());
        dto.setPayrollMonth(payslip.getPayrollMonth().toString());
        dto.setBasePay(payslip.getBasePay().toString());
        dto.setOvertimePay(payslip.getOvertimePay().toString());
        dto.setBonus(payslip.getBonus().toString());
        dto.setTotalEarnings(payslip.getTotalEarnings().toString());
        dto.setTaxDeduction(payslip.getTaxDeduction().toString());
        dto.setOtherDeductions(payslip.getOtherDeductions().toString());
        dto.setTotalDeductions(payslip.getTotalDeductions().toString());
        dto.setNetPay(payslip.getNetPay().toString());
        dto.setPdfPath(payslip.getPdfPath());
        dto.setCreatedAt(payslip.getCreatedAt().toString());
        return dto;
    }
}

