
package com.example.employee_management_system.repository;

import com.example.employee_management_system.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    // Check duplicate department name
    boolean existsByNameIgnoreCase(String name);

    // Find department by name
    Optional<Department> findByNameIgnoreCase(String name);
}
