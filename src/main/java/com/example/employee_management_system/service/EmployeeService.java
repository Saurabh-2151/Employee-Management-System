
package com.example.employee_management_system.service;

import com.example.employee_management_system.dto.request.EmployeeRequestDTO;
import com.example.employee_management_system.dto.response.EmployeeResponseDTO;

import com.example.employee_management_system.entity.Department;
import com.example.employee_management_system.entity.Employee;

import com.example.employee_management_system.mapper.EmployeeMapper;

import com.example.employee_management_system.repository.DepartmentRepository;
import com.example.employee_management_system.repository.EmployeeRepository;
import com.example.employee_management_system.repository.EmployeeSpecification;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // Constructor Injection
    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // Get all employees with search, filtering and pagination
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmployees(
            String search,
            Long departmentId,
            Boolean active,
            Pageable pageable
    ) {

        Specification<Employee> specification =
                EmployeeSpecification.filterEmployees(
                        search,
                        departmentId,
                        active
                );

        Page<Employee> employeePage =
                employeeRepository.findAll(specification, pageable);

        return employeePage.map(EmployeeMapper::toResponseDTO);
    }

    // Get employee by ID
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Employee not found with ID: " + id
                        ));

        return EmployeeMapper.toResponseDTO(employee);
    }

    // Create employee
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Employee details are required"
            );
        }

        Employee employee = EmployeeMapper.toEntity(dto);

        validateEmployee(employee);

        String email = normalizeEmail(employee.getEmail());

        if (employeeRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                    "Employee email already exists: " + email
            );
        }

        Long departmentId = dto.getDepartmentId();

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Department not found with ID: " + departmentId
                        ));

        employee.setFirstName(employee.getFirstName().trim());
        employee.setLastName(employee.getLastName().trim());
        employee.setEmail(email);
        employee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toResponseDTO(savedEmployee);
    }

    // Update employee
    @Transactional
    public EmployeeResponseDTO updateEmployee(
            Long id,
            EmployeeRequestDTO dto
    ) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Employee details are required"
            );
        }

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Employee not found with ID: " + id
                        ));

        Employee updatedEmployee = EmployeeMapper.toEntity(dto);

        validateEmployee(updatedEmployee);

        String email = normalizeEmail(updatedEmployee.getEmail());

        if (employeeRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new IllegalArgumentException(
                    "Employee email already exists: " + email
            );
        }

        Long departmentId = dto.getDepartmentId();

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Department not found with ID: " + departmentId
                        ));

        existingEmployee.setFirstName(
                updatedEmployee.getFirstName().trim()
        );

        existingEmployee.setLastName(
                updatedEmployee.getLastName().trim()
        );

        existingEmployee.setEmail(email);
        existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existingEmployee.setSalary(updatedEmployee.getSalary());
        existingEmployee.setDateOfJoining(updatedEmployee.getDateOfJoining());

        // Preserve existing status when active is not provided
        if (dto.getActive() != null) {
            existingEmployee.setActive(dto.getActive());
        }

        existingEmployee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(existingEmployee);

        return EmployeeMapper.toResponseDTO(savedEmployee);
    }

    // Soft Delete employee
    @Transactional
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Employee not found with ID: " + id
                        ));

        if (!employee.isActive()) {
            throw new IllegalArgumentException(
                    "Employee is already inactive"
            );
        }

        employee.setActive(false);

        employeeRepository.save(employee);
    }

    // Validate employee fields
    private void validateEmployee(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException(
                    "Employee details are required"
            );
        }

        if (employee.getFirstName() == null ||
                employee.getFirstName().isBlank()) {
            throw new IllegalArgumentException(
                    "First name is required"
            );
        }

        if (employee.getLastName() == null ||
                employee.getLastName().isBlank()) {
            throw new IllegalArgumentException(
                    "Last name is required"
            );
        }

        if (employee.getEmail() == null ||
                employee.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        if (employee.getSalary() == null ||
                employee.getSalary().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Salary must be greater than zero"
            );
        }

        if (employee.getDateOfJoining() == null) {
            throw new IllegalArgumentException(
                    "Date of joining is required"
            );
        }

        if (employee.getDepartment() == null ||
                employee.getDepartment().getId() == null) {
            throw new IllegalArgumentException(
                    "Department is required"
            );
        }
    }

    // Normalize email
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
