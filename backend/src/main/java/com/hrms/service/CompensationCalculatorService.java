package com.hrms.service;

import com.hrms.dto.CompensationCalculatorRequest;
import com.hrms.dto.CompensationCalculatorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Compensation Calculator Service
 * Calculates employee pay based on hourly rate, overtime, bonuses, and deductions
 */
@Service
@Slf4j
public class CompensationCalculatorService {

    /**
     * Calculate compensation based on provided parameters
     */
    public CompensationCalculatorResponse calculateCompensation(CompensationCalculatorRequest request) {
        BigDecimal baseSalary = new BigDecimal(request.getBaseSalary());
        BigDecimal hourlyRate = new BigDecimal(request.getHourlyRate());
        BigDecimal hoursWorked = new BigDecimal(request.getHoursWorked());
        BigDecimal overtimeMultiplier = new BigDecimal(request.getOvertimeMultiplier());
        BigDecimal bonusPercentage = new BigDecimal(request.getBonusPercentage() != null ? request.getBonusPercentage() : "0");
        BigDecimal taxPercentage = new BigDecimal(request.getTaxPercentage() != null ? request.getTaxPercentage() : "10");
        BigDecimal otherDeductions = new BigDecimal(request.getOtherDeductions() != null ? request.getOtherDeductions() : "0");

        // Regular hours (max 8 per day or 40 per week)
        BigDecimal regularHours = BigDecimal.valueOf(40);
        BigDecimal overtimeHours = hoursWorked.compareTo(regularHours) > 0
                ? hoursWorked.subtract(regularHours)
                : BigDecimal.ZERO;

        // Calculate base pay
        BigDecimal basePay = hourlyRate.multiply(hoursWorked).setScale(2, RoundingMode.HALF_UP);

        // Calculate overtime pay
        BigDecimal overtimeRate = hourlyRate.multiply(overtimeMultiplier);
        BigDecimal overtimePay = overtimeRate.multiply(overtimeHours).setScale(2, RoundingMode.HALF_UP);

        // Calculate bonus
        BigDecimal bonus = baseSalary.multiply(bonusPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Total earnings
        BigDecimal totalEarnings = basePay.add(overtimePay).add(bonus).setScale(2, RoundingMode.HALF_UP);

        // Calculate deductions
        BigDecimal taxDeduction = totalEarnings.multiply(taxPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalDeductions = taxDeduction.add(otherDeductions).setScale(2, RoundingMode.HALF_UP);

        // Net pay
        BigDecimal netPay = totalEarnings.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);

        // Build response
        CompensationCalculatorResponse response = new CompensationCalculatorResponse();
        response.setBasePay(basePay.toString());
        response.setOvertimePay(overtimePay.toString());
        response.setBonus(bonus.toString());
        response.setTotalEarnings(totalEarnings.toString());
        response.setTaxDeduction(taxDeduction.toString());
        response.setOtherDeductions(otherDeductions.toString());
        response.setTotalDeductions(totalDeductions.toString());
        response.setNetPay(netPay.toString());

        response.setHoursWorked(hoursWorked.toString());
        response.setOvertimeHours(overtimeHours.toString());
        response.setRegularHours(regularHours.toString());
        response.setHourlyRate(hourlyRate.toString());
        response.setOvertimeRate(overtimeRate.toString());

        log.debug("Compensation calculated for hours: {}, net pay: {}", hoursWorked, netPay);
        return response;
    }

    /**
     * Calculate monthly compensation
     */
    public CompensationCalculatorResponse calculateMonthlyCompensation(
            BigDecimal baseSalary,
            BigDecimal overtimeHours,
            BigDecimal taxDeduction,  
            BigDecimal overtimeMultiplier,
            BigDecimal bonusPercentage,
            BigDecimal taxPercentage,
            BigDecimal otherDeductions) {

        // Calculate monthly overtime pay
        BigDecimal hourlyRate = baseSalary.divide(BigDecimal.valueOf(160), 2, RoundingMode.HALF_UP); // 160 hours per month
        BigDecimal overtimeRate = hourlyRate.multiply(overtimeMultiplier);
        BigDecimal overtimePay = overtimeRate.multiply(overtimeHours).setScale(2, RoundingMode.HALF_UP);

        // Calculate bonus
        BigDecimal bonus = baseSalary.multiply(bonusPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Total earnings
        BigDecimal totalEarnings = baseSalary.add(overtimePay).add(bonus).setScale(2, RoundingMode.HALF_UP);

        // Calculate deductions
        BigDecimal taxDeduction = totalEarnings.multiply(taxPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalDeductions = taxDeduction.add(otherDeductions).setScale(2, RoundingMode.HALF_UP);

        // Net pay
        BigDecimal netPay = totalEarnings.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);

        // Build response
        CompensationCalculatorResponse response = new CompensationCalculatorResponse();
        response.setBasePay(baseSalary.toString());
        response.setOvertimePay(overtimePay.toString());
        response.setBonus(bonus.toString());
        response.setTotalEarnings(totalEarnings.toString());
        response.setTaxDeduction(taxDeduction.toString());
        response.setOtherDeductions(otherDeductions.toString());
        response.setTotalDeductions(totalDeductions.toString());
        response.setNetPay(netPay.toString());

        response.setOvertimeHours(overtimeHours.toString());
        response.setHourlyRate(hourlyRate.toString());
        response.setOvertimeRate(overtimeRate.toString());

        log.debug("Monthly compensation calculated, net pay: {}", netPay);
        return response;
    }
}

