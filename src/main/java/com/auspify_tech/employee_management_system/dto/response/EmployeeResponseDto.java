package com.auspify_tech.employee_management_system.dto.response;

import com.auspify_tech.employee_management_system.model.EmployeeStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record EmployeeResponseDto(String id,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  String phoneNumber,
                                  LocalDate hireDate,
                                  BigDecimal salary,
                                  EmployeeStatus status,
                                  String departmentId,
                                  String departmentCode,
                                  Instant createdAt,
                                  Instant updatedAt) {
}
