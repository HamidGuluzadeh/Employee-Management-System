package com.auspify_tech.employee_management_system.service;

import com.auspify_tech.employee_management_system.dto.request.CreateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.request.UpdateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.response.EmployeeResponseDto;
import com.auspify_tech.employee_management_system.model.EmployeeStatus;
import org.springframework.data.domain.Page;

public interface EmployeeService {

    Page<EmployeeResponseDto> getEmployeesByDepartment(String departmentId, int page, int size);

    EmployeeResponseDto addEmployee(CreateEmployeeRequestDto request);

    EmployeeResponseDto updateEmployee(String employeeId, UpdateEmployeeRequestDto request);

    void changeEmployeeStatus(String employeeId, EmployeeStatus status);

}
