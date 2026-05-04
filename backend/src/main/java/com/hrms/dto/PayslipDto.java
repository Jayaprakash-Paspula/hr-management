package com.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Payslip DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayslipDto {
    private String id;
    private String employeeId;
    private String employeeName;
    private String payrollMonth;
    private String basePay;
    private String overtimePay;
    private String bonus;
    private String totalEarnings;
    private String taxDeduction;
    private String otherDeductions;
    private String totalDeductions;
    private String netPay;
    private String pdfPath;
    private String createdAt;
    private List<PayslipDetailDto> details;
}

/**
 * Payslip Detail DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class PayslipDetailDto {
    private String id;
    private String description;
    private String amount;
    private String category; // EARNINGS, DEDUCTION
}

