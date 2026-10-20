package com.auspify_tech.employee_management_system.dto.request;

import com.auspify_tech.employee_management_system.model.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeRequestDto(@NotBlank(message = "First name cannot be empty!")
                                     @Size(max = 10, message = "First name cannot exceed 10 characters!")
                                     String firstName,
                                       @NotBlank(message = "Last name cannot be empty!")
                                 @Size(max = 10, message = "Last name cannot exceed 15 characters!")
                                 String lastName,
                                       @NotBlank(message = "Email cannot be empty!")
                                 @Email(message = "Enter valid email format!")
                                 @Size(max = 50, message = "Email name cannot exceed 50 characters!")
                                 String email,
                                       @NotBlank(message = "Phone number cannot be empty!")
                                 @Size(max = 20, message = "Phone number cannot exceed 20 characters!")
                                 String phoneNumber,
                                       @NotNull(message = "Hire date cannot be empty!")
                                 LocalDate hireDate,
                                       @NotNull(message = "Salary cannot be empty!")
                                 BigDecimal salary,
                                       @NotNull(message = "Status cannot be empty!")
                                 EmployeeStatus status,
                                       @NotBlank(message = "Department ID cannot be empty!")
                                 String departmentId) {

}
