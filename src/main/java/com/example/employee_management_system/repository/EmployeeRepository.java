
package com.example.employee_management_system.repository;

import com.example.employee_management_system.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends
        JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    // Check duplicate email
    boolean existsByEmailIgnoreCase(String email);

    // Check duplicate email excluding current employee
    boolean existsByEmailIgnoreCaseAndIdNot(
            String email, Long id
    );

    // Find employee by email
    Optional<Employee> findByEmailIgnoreCase(String email);
}
