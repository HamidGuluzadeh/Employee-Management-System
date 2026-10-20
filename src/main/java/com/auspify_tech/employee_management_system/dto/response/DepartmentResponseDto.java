package com.auspify_tech.employee_management_system.dto.response;

import java.time.Instant;

public record DepartmentResponseDto(String id,
                                    String name,
                                    String code,
                                    Instant createdAt,
                                    Instant updatedAt) {

}
