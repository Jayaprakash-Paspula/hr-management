package com.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compensation Calculator Response DTO (with breakdown)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompensationCalculatorResponse {
    private String basePay;
    private String overtimePay;
    private String bonus;
    private String totalEarnings;
    private String taxDeduction;
    private String otherDeductions;
    private String totalDeductions;
    private String netPay;

    // Breakdown details
    private String hoursWorked;
    private String overtimeHours;
    private String regularHours;
    private String hourlyRate;
    private String overtimeRate;
}

