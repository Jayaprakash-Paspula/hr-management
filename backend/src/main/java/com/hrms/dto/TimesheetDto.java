package com.hrms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Timesheet DTO
 */
@Data
@NoArgsConstructor
public class TimesheetDto {
    private String id;

    @NotBlank
    private String employeeId;

    @NotBlank
    private String timesheetDate;

    @NotNull
    private String hoursWorked;

    private String project;

    private String taskDescription;

    private String status;

    private String submittedAt;

    private String createdAt;

    private String updatedAt;

    private String employeeName;
}

