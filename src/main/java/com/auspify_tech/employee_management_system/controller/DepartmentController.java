package com.auspify_tech.employee_management_system.controller;

import com.auspify_tech.employee_management_system.dto.SuccessDto;
import com.auspify_tech.employee_management_system.dto.request.DepartmentRequestDto;
import com.auspify_tech.employee_management_system.dto.response.DepartmentResponseDto;
import com.auspify_tech.employee_management_system.model.SuccessStatus;
import com.auspify_tech.employee_management_system.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping("/all")
    public ResponseEntity<SuccessDto<List<DepartmentResponseDto>>> getAllDepartments() {
        List<DepartmentResponseDto> responseDtoList = departmentService.getAllDepartments();
        SuccessDto<List<DepartmentResponseDto>> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDtoList);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<SuccessDto<DepartmentResponseDto>> getDepartmentById(@PathVariable String departmentId) {
        DepartmentResponseDto responseDto = departmentService.getDepartmentById(departmentId);
        SuccessDto<DepartmentResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDto);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping("/code/{departmentCode}")
    public ResponseEntity<SuccessDto<DepartmentResponseDto>> getDepartmentByCode(@PathVariable String departmentCode) {
        DepartmentResponseDto responseDto = departmentService.getDepartmentByCode(departmentCode);
        SuccessDto<DepartmentResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDto);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<SuccessDto<DepartmentResponseDto>> addDepartment(@RequestBody @Valid DepartmentRequestDto request) {
        DepartmentResponseDto responseDto = departmentService.addDepartment(request);
        SuccessDto<DepartmentResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDto);
        return new ResponseEntity<>(successDto, HttpStatus.CREATED);
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<SuccessDto<DepartmentResponseDto>> updateDepartment(@PathVariable String departmentId,
                                                                              @RequestBody @Valid DepartmentRequestDto request) {
        DepartmentResponseDto responseDto = departmentService.updateDepartment(departmentId, request);
        SuccessDto<DepartmentResponseDto> successDto = new SuccessDto<>(SuccessStatus.SUCCESS, responseDto);
        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }
}
