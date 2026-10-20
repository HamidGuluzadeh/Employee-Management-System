package com.auspify_tech.employee_management_system.repository;

import com.auspify_tech.employee_management_system.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {

    Optional<DepartmentEntity> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByName(String name);

}
