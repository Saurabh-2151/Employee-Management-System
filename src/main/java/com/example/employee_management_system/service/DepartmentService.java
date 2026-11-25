
package com.example.employee_management_system.service;

import com.example.employee_management_system.dto.request.DepartmentRequestDTO;
import com.example.employee_management_system.dto.response.DepartmentResponseDTO;

import com.example.employee_management_system.entity.Department;

import com.example.employee_management_system.mapper.DepartmentMapper;

import com.example.employee_management_system.repository.DepartmentRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    // Constructor Injection
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Get all departments
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toResponseDTO)
                .toList();
    }

    // Get department by ID
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Department not found with ID: " + id
                        ));

        return DepartmentMapper.toResponseDTO(department);
    }

    // Create department
    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {

        if (dto == null ||
                dto.getName() == null ||
                dto.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Department name is required"
            );
        }

        String name = dto.getName().trim();

        if (departmentRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException(
                    "Department already exists: " + name
            );
        }

        Department department = DepartmentMapper.toEntity(dto);

        department.setName(name);

        Department savedDepartment = departmentRepository.save(department);

        return DepartmentMapper.toResponseDTO(savedDepartment);
    }

    // Update department
    @Transactional
    public DepartmentResponseDTO updateDepartment(
            Long id,
            DepartmentRequestDTO dto
    ) {

        if (dto == null ||
                dto.getName() == null ||
                dto.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Department name is required"
            );
        }

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Department not found with ID: " + id
                        ));

        String name = dto.getName().trim();

        boolean duplicate = departmentRepository
                .findByNameIgnoreCase(name)
                .filter(dept -> !dept.getId().equals(id))
                .isPresent();

        if (duplicate) {
            throw new IllegalArgumentException(
                    "Department already exists: " + name
            );
        }

        existingDepartment.setName(name);
        existingDepartment.setDescription(dto.getDescription());

        Department savedDepartment =
                departmentRepository.save(existingDepartment);

        return DepartmentMapper.toResponseDTO(savedDepartment);
    }

    // Delete department
    @Transactional
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Department not found with ID: " + id
                        ));

        departmentRepository.delete(department);
    }
}
