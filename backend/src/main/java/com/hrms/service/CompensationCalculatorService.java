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

    private static final BigDecimal DEFAULT_TAX_PERCENTAGE = BigDecimal.valueOf(10);
    private static final BigDecimal DEFAULT_BONUS_PERCENTAGE = BigDecimal.ZERO;
    private static final BigDecimal DEFAULT_OTHER_DEDUCTIONS = BigDecimal.ZERO;
    private static final BigDecimal REGULAR_WEEKLY_HOURS = BigDecimal.valueOf(40);
    private static final BigDecimal MONTHLY_WORKING_HOURS = BigDecimal.valueOf(160);

    /**
     * Calculate compensation based on provided parameters
     */
    public CompensationCalculatorResponse calculateCompensation(CompensationCalculatorRequest request) {
        BigDecimal baseSalary = safeBigDecimal(request.getBaseSalary());
        BigDecimal hourlyRate = safeBigDecimal(request.getHourlyRate());
        BigDecimal hoursWorked = safeBigDecimal(request.getHoursWorked());
        BigDecimal overtimeMultiplier = safeBigDecimal(request.getOvertimeMultiplier());
        BigDecimal bonusPercentage = request.getBonusPercentage() != null ? safeBigDecimal(request.getBonusPercentage()) : DEFAULT_BONUS_PERCENTAGE;
        BigDecimal taxPercentage = request.getTaxPercentage() != null ? safeBigDecimal(request.getTaxPercentage()) : DEFAULT_TAX_PERCENTAGE;
        BigDecimal otherDeductions = request.getOtherDeductions() != null ? safeBigDecimal(request.getOtherDeductions()) : DEFAULT_OTHER_DEDUCTIONS;

        // Regular vs Overtime hours
        BigDecimal overtimeHours = hoursWorked.compareTo(REGULAR_WEEKLY_HOURS) > 0
                ? hoursWorked.subtract(REGULAR_WEEKLY_HOURS)
                : BigDecimal.ZERO;

        // Base pay
        BigDecimal basePay = hourlyRate.multiply(hoursWorked).setScale(2, RoundingMode.HALF_UP);

        // Overtime pay
        BigDecimal overtimeRate = hourlyRate.multiply(overtimeMultiplier);
        BigDecimal overtimePay = overtimeRate.multiply(overtimeHours).setScale(2, RoundingMode.HALF_UP);

        // Bonus
        BigDecimal bonus = baseSalary.multiply(bonusPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Total earnings
        BigDecimal totalEarnings = basePay.add(overtimePay).add(bonus).setScale(2, RoundingMode.HALF_UP);

        // Deductions
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
        response.setRegularHours(REGULAR_WEEKLY_HOURS.toString());
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
            BigDecimal overtimeMultiplier,
            BigDecimal bonusPercentage,
            BigDecimal taxPercentage,
            BigDecimal otherDeductions) {

        // Hourly rate based on 160 hours per month
        BigDecimal hourlyRate = baseSalary.divide(MONTHLY_WORKING_HOURS, 2, RoundingMode.HALF_UP);
        BigDecimal overtimeRate = hourlyRate.multiply(overtimeMultiplier);
        BigDecimal overtimePay = overtimeRate.multiply(overtimeHours).setScale(2, RoundingMode.HALF_UP);

        // Bonus
        BigDecimal bonus = baseSalary.multiply(bonusPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Total earnings
        BigDecimal totalEarnings = baseSalary.add(overtimePay).add(bonus).setScale(2, RoundingMode.HALF_UP);

        // Deductions
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

    /**
     * Utility method to safely convert to BigDecimal
     */
    private BigDecimal safeBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
