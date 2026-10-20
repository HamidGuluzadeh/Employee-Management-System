package com.auspify_tech.employee_management_system.service.implement;

import com.auspify_tech.employee_management_system.dto.request.CreateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.request.UpdateEmployeeRequestDto;
import com.auspify_tech.employee_management_system.dto.response.EmployeeResponseDto;
import com.auspify_tech.employee_management_system.entity.DepartmentEntity;
import com.auspify_tech.employee_management_system.entity.EmployeeEntity;
import com.auspify_tech.employee_management_system.exception.BadRequestException;
import com.auspify_tech.employee_management_system.exception.ConflictException;
import com.auspify_tech.employee_management_system.exception.ResourceAlreadyExistsException;
import com.auspify_tech.employee_management_system.exception.ResourceNotFoundException;
import com.auspify_tech.employee_management_system.mapper.EmployeeMapper;
import com.auspify_tech.employee_management_system.model.EmployeeStatus;
import com.auspify_tech.employee_management_system.repository.DepartmentRepository;
import com.auspify_tech.employee_management_system.repository.EmployeeRepository;
import com.auspify_tech.employee_management_system.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeRepository employeeRepository;
    DepartmentRepository departmentRepository;
    EmployeeMapper employeeMapper;

    @Override
    public Page<EmployeeResponseDto> getEmployeesByDepartment(String departmentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<EmployeeEntity> employeeEntities =  employeeRepository.findAllByDepartmentId(departmentId, pageable);

        return employeeEntities.map(employeeMapper::mapEntityToResponse);
    }

    @Override
    @Transactional
    public EmployeeResponseDto addEmployee(CreateEmployeeRequestDto request) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Employee already exists with email: " + request.email());
        }

        if (employeeRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new ResourceAlreadyExistsException("Employee already exists with phone number: " + request.phoneNumber());
        }

        EmployeeEntity employeeEntity = employeeMapper.mapRequestToEntity(request);

        DepartmentEntity departmentEntity = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found!"));

        employeeEntity.setDepartment(departmentEntity);
        employeeEntity.setCreatedAt(Instant.now());

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

        return employeeMapper.mapEntityToResponse(savedEmployeeEntity);
    }

    @Override
    @Transactional
    public EmployeeResponseDto updateEmployee(String employeeId, UpdateEmployeeRequestDto request) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found!"));

        if (employeeRepository.existsByEmailAndIdNot(request.email(), employeeId)) {
            throw new ResourceAlreadyExistsException("Email already exists for another employee!");
        }

        if (employeeRepository.existsByPhoneNumberAndIdNot(request.phoneNumber(), employeeId)) {
            throw new ResourceAlreadyExistsException("Phone number already exists for another employee!");
        }

        employeeMapper.updateEntityFromRequest(request, employeeEntity);

        if (!employeeEntity.getDepartment().getId().equals(request.departmentId())) {
            DepartmentEntity departmentEntity = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found!"));

            employeeEntity.setDepartment(departmentEntity);
        }

        employeeEntity.setUpdatedAt(Instant.now());

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

        return employeeMapper.mapEntityToResponse(savedEmployeeEntity);
    }

    @Override
    @Transactional
    public void changeEmployeeStatus(String employeeId, EmployeeStatus status) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found!"));

        if (employeeEntity.getStatus().equals(EmployeeStatus.TERMINATED)) {
            throw new BadRequestException("Employee has already been terminated!");
        }

        employeeEntity.setStatus(status);
        employeeEntity.setUpdatedAt(Instant.now());

        employeeRepository.save(employeeEntity);
    }
}
