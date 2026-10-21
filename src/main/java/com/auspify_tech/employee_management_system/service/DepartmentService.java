package com.auspify_tech.employee_management_system.service;

import com.auspify_tech.employee_management_system.dto.request.DepartmentRequestDto;
import com.auspify_tech.employee_management_system.dto.response.DepartmentResponseDto;

import java.util.List;

public interface
DepartmentService {

    List<DepartmentResponseDto> getAllDepartments();

    DepartmentResponseDto getDepartmentById(String departmentId);

    DepartmentResponseDto getDepartmentByCode(String departmentCode);

    DepartmentResponseDto addDepartment(DepartmentRequestDto request);

    DepartmentResponseDto updateDepartment(String departmentId, DepartmentRequestDto request);

    void deleteDepartment(String departmentId);

}
