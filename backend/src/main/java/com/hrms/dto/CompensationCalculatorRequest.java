package com.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compensation Calculator Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompensationCalculatorRequest {
    private String employeeId;
    private String baseSalary;
    private String hourlyRate;
    private String hoursWorked;
    private String overtimeMultiplier;
    private String bonusPercentage;
    private String taxPercentage;
    private String otherDeductions;
}


