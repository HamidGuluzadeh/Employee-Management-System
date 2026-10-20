package com.auspify_tech.employee_management_system.mapper;

import com.auspify_tech.employee_management_system.dto.request.DepartmentRequestDto;
import com.auspify_tech.employee_management_system.dto.response.DepartmentResponseDto;
import com.auspify_tech.employee_management_system.entity.DepartmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {

    DepartmentResponseDto mapEntityToResponse(DepartmentEntity department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DepartmentEntity mapRequestToEntity(DepartmentRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(DepartmentRequestDto request, @MappingTarget DepartmentEntity department);

}
