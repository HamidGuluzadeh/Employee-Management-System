package com.auspify_tech.employee_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateEmployeeRequestDto(@Size(max = 10, message = "First name cannot exceed 10 characters!")
                                       String firstName,
                                       @Size(max = 10, message = "Last name cannot exceed 15 characters!")
                                       String lastName,
                                       @Email(message = "Enter valid email format!")
                                       @Size(max = 50, message = "Email name cannot exceed 50 characters!")
                                       String email,
                                       @Size(max = 20, message = "Phone number cannot exceed 20 characters!")
                                       String phoneNumber,
                                       BigDecimal salary,
                                       String departmentId) {

}
