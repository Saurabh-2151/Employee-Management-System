
package com.example.employee_management_system.service;

import com.example.employee_management_system.dto.request.DepartmentRequestDTO;
import com.example.employee_management_system.dto.response.DepartmentResponseDTO;
import com.example.employee_management_system.entity.Department;
import com.example.employee_management_system.repository.DepartmentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    // Test 1: Get all departments
    @Test
    void getAllDepartments_ShouldReturnDepartmentList() {

        Department department = new Department();
        department.setId(1L);
        department.setName("IT");
        department.setDescription("IT Department");

        when(departmentRepository.findAll())
                .thenReturn(List.of(department));

        List<DepartmentResponseDTO> result =
                departmentService.getAllDepartments();

        assertEquals(1, result.size());
        assertEquals("IT", result.get(0).getName());

        verify(departmentRepository, times(1)).findAll();
    }

    // Test 2: Create department successfully
    @Test
    void createDepartment_ShouldCreateDepartmentSuccessfully() {

        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName("HR");
        dto.setDescription("Human Resources");

        when(departmentRepository.existsByNameIgnoreCase("HR"))
                .thenReturn(false);

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> {
                    Department department = invocation.getArgument(0);
                    department.setId(1L);
                    return department;
                });

        DepartmentResponseDTO result =
                departmentService.createDepartment(dto);

        assertNotNull(result);
        assertEquals("HR", result.getName());
        assertEquals("Human Resources", result.getDescription());

        verify(departmentRepository).save(any(Department.class));
    }

    // Test 3: Duplicate department name
    @Test
    void createDepartment_ShouldThrowExceptionForDuplicateName() {

        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName("IT");
        dto.setDescription("IT Department");

        when(departmentRepository.existsByNameIgnoreCase("IT"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> departmentService.createDepartment(dto)
        );

        verify(departmentRepository, never())
                .save(any(Department.class));
    }
}
