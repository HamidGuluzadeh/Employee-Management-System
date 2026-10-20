package com.auspify_tech.employee_management_system.repository;

import com.auspify_tech.employee_management_system.entity.DepartmentEntity;
import com.auspify_tech.employee_management_system.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String> {

    boolean existsByDepartmentId(String departmentId);

}
