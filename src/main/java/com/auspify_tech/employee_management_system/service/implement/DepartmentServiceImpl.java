package com.auspify_tech.employee_management_system.service.implement;

import com.auspify_tech.employee_management_system.dto.request.DepartmentRequestDto;
import com.auspify_tech.employee_management_system.dto.response.DepartmentResponseDto;
import com.auspify_tech.employee_management_system.entity.DepartmentEntity;
import com.auspify_tech.employee_management_system.exception.ConflictException;
import com.auspify_tech.employee_management_system.exception.ResourceAlreadyExistsException;
import com.auspify_tech.employee_management_system.exception.ResourceNotFoundException;
import com.auspify_tech.employee_management_system.mapper.DepartmentMapper;
import com.auspify_tech.employee_management_system.repository.DepartmentRepository;
import com.auspify_tech.employee_management_system.repository.EmployeeRepository;
import com.auspify_tech.employee_management_system.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentServiceImpl implements DepartmentService {
    DepartmentRepository departmentRepository;
    EmployeeRepository employeeRepository;
    DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();

        List<DepartmentResponseDto> departmentResponseDtoList = new ArrayList<>();

        for (DepartmentEntity departmentEntity : departmentEntities) {
            DepartmentResponseDto departmentResponseDto = departmentMapper.mapEntityToResponse(departmentEntity);
            departmentResponseDtoList.add(departmentResponseDto);
        }

        return departmentResponseDtoList;
    }

    @Override
    public DepartmentResponseDto getDepartmentById(String departmentId) {
        DepartmentEntity departmentEntity = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found!"));

        return departmentMapper.mapEntityToResponse(departmentEntity);
    }

    @Override
    public DepartmentResponseDto getDepartmentByCode(String departmentCode) {
        DepartmentEntity departmentEntity = departmentRepository.findByCode(departmentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found!"));

        return departmentMapper.mapEntityToResponse(departmentEntity);
    }

    @Override
    @Transactional
    public DepartmentResponseDto addDepartment(DepartmentRequestDto request) {
        if (departmentRepository.existsByCode(request.code())) {
            throw new ResourceAlreadyExistsException(request.code() + " department already exists!");
        }

        if (departmentRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException(request.name() + " department already exists!");
        }

        DepartmentEntity departmentEntity = departmentMapper.mapRequestToEntity(request);
        departmentEntity.setCreatedAt(Instant.now());

        DepartmentEntity savedDepartmentEntity = departmentRepository.save(departmentEntity);

        return departmentMapper.mapEntityToResponse(savedDepartmentEntity);
    }

    @Override
    @Transactional
    public DepartmentResponseDto updateDepartment(String departmentId, DepartmentRequestDto request) {
        DepartmentEntity departmentEntity = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found!"));

        if (!departmentEntity.getCode().equals(request.code()) &&
                departmentRepository.existsByCode(request.code())) {
            throw new ResourceAlreadyExistsException(request.code() + " department already exists!");
        }

        if (!departmentEntity.getName().equals(request.name()) &&
                departmentRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException(request.name() + " department already exists!");
        }

        departmentMapper.updateEntityFromRequest(request, departmentEntity);
        departmentEntity.setUpdatedAt(Instant.now());

        DepartmentEntity savedDepartmentEntity = departmentRepository.save(departmentEntity);

        return departmentMapper.mapEntityToResponse(savedDepartmentEntity);
    }

    @Override
    @Transactional
    public void deleteDepartment(String departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found!");
        }

        if (employeeRepository.existsByDepartmentId(departmentId)) {
            throw new ConflictException("Department cannot be deleted due to having employees!");
        }

        departmentRepository.deleteById(departmentId);
    }
}
