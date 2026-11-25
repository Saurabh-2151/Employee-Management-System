
package com.example.employee_management_system.service;

import com.example.employee_management_system.dto.request.EmployeeRequestDTO;
import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.repository.DepartmentRepository;
import com.example.employee_management_system.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    // Test 1: Duplicate email should not create employee
    @Test
    void createEmployee_ShouldThrowException_WhenEmailAlreadyExists() {

        EmployeeRequestDTO dto = new EmployeeRequestDTO();

        dto.setFirstName("Rahul");
        dto.setLastName("Patil");
        dto.setEmail("rahul@gmail.com");
        dto.setPhoneNumber("9876543210");
        dto.setSalary(new BigDecimal("35000"));
        dto.setDateOfJoining(LocalDate.now());
        dto.setDepartmentId(1L);

        when(employeeRepository.existsByEmailIgnoreCase("rahul@gmail.com"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.createEmployee(dto)
        );

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // Test 2: Employee not found should throw exception
    @Test
    void getEmployeeById_ShouldThrowException_WhenEmployeeDoesNotExist() {

        Long employeeId = 999L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> employeeService.getEmployeeById(employeeId)
        );
    }

    // Test 3: Delete employee should perform soft delete
    @Test
    void deleteEmployee_ShouldSetActiveFalse_WhenEmployeeExists() {

        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setFirstName("Rahul");
        employee.setActive(true);

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        employeeService.deleteEmployee(employeeId);

        assertFalse(employee.isActive());

        verify(employeeRepository).save(employee);
    }
}
