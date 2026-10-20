package com.auspify_tech.employee_management_system.entity;

import com.auspify_tech.employee_management_system.model.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    String id;

    @Column(name = "first_name", length = 10, nullable = false)
    String firstName;

    @Column(name = "last_name", length = 15, nullable = false)
    String lastName;

    @Column(name = "email", length = 50, nullable = false)
    String email;

    @Column(name = "phone_number", length = 20, nullable = false, unique = true)
    String phoneNumber;

    @Column(name = "hire_date", nullable = false)
    LocalDate hireDate;

    @Column(name = "salary", nullable = false, precision =  6, scale = 2)
    BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    EmployeeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    DepartmentEntity department;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;
}
