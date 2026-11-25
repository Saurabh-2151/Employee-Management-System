
package com.example.employee_management_system.mapper;

import com.example.employee_management_system.dto.request.DepartmentRequestDTO;
import com.example.employee_management_system.dto.response.DepartmentResponseDTO;
import com.example.employee_management_system.entity.Department;

public class DepartmentMapper {

    public static Department toEntity(DepartmentRequestDTO dto) {

        Department department = new Department();

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        return department;
    }

    public static DepartmentResponseDTO toResponseDTO(Department department) {

        DepartmentResponseDTO dto = new DepartmentResponseDTO();

        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());

        return dto;
    }
}
