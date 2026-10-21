package com.auspify_tech.employee_management_system.controller;

import com.auspify_tech.employee_management_system.dto.SuccessDto;
import com.auspify_tech.employee_management_system.dto.request.CreateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.request.UpdateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.response.EmployeeResponseDto;
import com.auspify_tech.employee_management_system.model.EmployeeStatus;
import com.auspify_tech.employee_management_system.model.SuccessStatus;
import com.auspify_tech.employee_management_system.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {
    EmployeeService employeeService;

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<SuccessDto<Page<EmployeeResponseDto>>> getEmployeesByDepartmentId(@PathVariable String departmentId,
                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "20") int size) {
        Page<EmployeeResponseDto> pageResponseDto = employeeService.getEmployeesByDepartmentId(departmentId, page, size);
        SuccessDto<Page<EmployeeResponseDto>> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, pageResponseDto);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/department/code/{departmentCode}")
    public ResponseEntity<SuccessDto<Page<EmployeeResponseDto>>> getEmployeesByDepartmentCode(@PathVariable String departmentCode,
                                                                                              @RequestParam(defaultValue = "0") int page,
                                                                                              @RequestParam(defaultValue = "20") int size) {
        Page<EmployeeResponseDto> pageResponseDto = employeeService.getEmployeesByDepartmentCode(departmentCode, page, size);
        SuccessDto<Page<EmployeeResponseDto>> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, pageResponseDto);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<SuccessDto<EmployeeResponseDto>> getEmployeeById(@PathVariable String employeeId) {
        EmployeeResponseDto responseDto = employeeService.getEmployeeById(employeeId);
        SuccessDto<EmployeeResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDto);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<SuccessDto<EmployeeResponseDto>> addEmployee(@RequestBody @Valid CreateEmployeeRequestDto request) {
        EmployeeResponseDto responseDto = employeeService.addEmployee(request);
        SuccessDto<EmployeeResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDto);
        return new ResponseEntity<>(successDto, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<SuccessDto<EmployeeResponseDto>> updateEmployee(@PathVariable String employeeId,
                                                                          @RequestBody @Valid UpdateEmployeeRequestDto request) {
        EmployeeResponseDto responseDto = employeeService.updateEmployee(employeeId, request);
        SuccessDto<EmployeeResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDto);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PatchMapping("/{employeeId}")
    public ResponseEntity<Void> changeEmployeeStatus(@PathVariable String employeeId,
                                                     @RequestParam EmployeeStatus status) {
        employeeService.changeEmployeeStatus(employeeId, status);
        return ResponseEntity.ok().build();
    }
}
