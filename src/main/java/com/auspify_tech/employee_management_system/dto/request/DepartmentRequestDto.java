package com.auspify_tech.employee_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequestDto(@NotBlank(message = "Department name cannot be empty!")
                                   @Size(max = 40, message = "Department name cannot exceed 40 characters!")
                                   String name,
                                   @NotBlank(message = "Department code cannot be empty!")
                                   @Size(max = 10, message = "Department code cannot exceed 10 characters!")
                                   String code) {

}
