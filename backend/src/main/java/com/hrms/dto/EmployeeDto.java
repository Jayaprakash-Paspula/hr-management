package com.hrms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee DTO for API requests/responses
 */
@Data
@NoArgsConstructor
public class EmployeeDto {
    private String id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotBlank
    private String departmentId;

    @NotBlank
    private String designation;

    @NotBlank
    private String joiningDate;

    @NotBlank
    private String baseSalary;

    private String hourlyRate;

    private String overtimeRate;

    private String managerId;

    private boolean active;

    private String createdAt;

    private String updatedAt;

    private String departmentName;

    private String managerName;
}


