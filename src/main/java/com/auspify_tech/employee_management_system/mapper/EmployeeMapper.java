package com.auspify_tech.employee_management_system.mapper;

import com.auspify_tech.employee_management_system.dto.request.CreateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.request.UpdateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.response.EmployeeResponseDto;
import com.auspify_tech.employee_management_system.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    // DÜZƏLİŞ: id/createdAt/updatedAt artıq ignore edilmir, departmentId/departmentCode isə department-dən götürülür
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentCode", source = "department.code")
    EmployeeResponseDto mapEntityToResponse(EmployeeEntity employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployeeEntity mapRequestToEntity(CreateEmployeeRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateEmployeeRequestDto request, @MappingTarget EmployeeEntity employee);
}
