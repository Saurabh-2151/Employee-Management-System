
package com.example.employee_management_system.mapper;

import com.example.employee_management_system.dto.request.EmployeeRequestDTO;
import com.example.employee_management_system.dto.response.EmployeeResponseDTO;
import com.example.employee_management_system.entity.Department;
import com.example.employee_management_system.entity.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDTO dto) {

        Employee employee = new Employee();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setSalary(dto.getSalary());
        employee.setDateOfJoining(dto.getDateOfJoining());

        if (dto.getActive() != null) {
            employee.setActive(dto.getActive());
        }

        Department department = new Department();
        department.setId(dto.getDepartmentId());

        employee.setDepartment(department);

        return employee;
    }

    public static EmployeeResponseDTO toResponseDTO(Employee employee) {

        EmployeeResponseDTO dto = new EmployeeResponseDTO();

        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setSalary(employee.getSalary());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setActive(employee.isActive());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }

        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        return dto;
    }
}
