package com.effigo.tools.support_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.effigo.tools.support_api.dto.RoleDTO;
import com.effigo.tools.support_api.exception.DuplicateResourceException;
import com.effigo.tools.support_api.exception.ResourceNotFoundException;
import com.effigo.tools.support_api.model.Role;
import com.effigo.tools.support_api.repository.RoleRepository;
import com.effigo.tools.support_api.service.serviceImpl.RoleServiceImpl;

@ExtendWith(MockitoExtension.class) 
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository; // Mock the database

    @Mock
    private ModelMapper modelMapper; // Mock ModelMapper

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setRoleName("Admin");
        role.setCreatedOn(new Timestamp(System.currentTimeMillis()));
        role.setCreatedBy("System");

        roleDTO = new RoleDTO();
        roleDTO.setRoleName("Admin");
    }

    // Positive Test Case: Create Role Successfully
    @Test
    void testCreateRole_Success() {
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(modelMapper.map(any(RoleDTO.class), eq(Role.class))).thenReturn(role);
        when(modelMapper.map(any(Role.class), eq(RoleDTO.class))).thenReturn(roleDTO);

        RoleDTO createdRole = roleService.createRole(roleDTO);
        
        assertNotNull(createdRole);
        assertEquals("Admin", createdRole.getRoleName());
    }

 // Negative Test Case: Creating Duplicate Role
    @Test
    void testCreateRole_DuplicateRoleName() {
        when(roleRepository.existsByRoleName("Admin")).thenReturn(true);

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            roleService.createRole(roleDTO);
        });

        assertEquals("Role with name 'Admin' already exists.", exception.getMessage());
    }


    //Positive Test Case: Fetch Role by ID
    @Test
    void testGetRoleById_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(modelMapper.map(any(Role.class), eq(RoleDTO.class))).thenReturn(roleDTO);

        RoleDTO foundRole = roleService.getRoleById(1L);

        assertNotNull(foundRole);
        assertEquals("Admin", foundRole.getRoleName());
    }

    //Negative Test Case: Fetch Non-Existing Role
    @Test
    void testGetRoleById_NotFound() {
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            roleService.getRoleById(2L);
        });

        assertEquals("Role not found with ID: 2", exception.getMessage());
    }

    // Positive Test Case: Delete Role Successfully
    @Test
    void testDeleteRole_Success() {
        when(roleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(roleRepository).deleteById(1L);

        assertDoesNotThrow(() -> roleService.deleteRole(1L));
    }

    //Negative Test Case: Delete Non-Existing Role
    @Test
    void testDeleteRole_NotFound() {
        when(roleRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            roleService.deleteRole(2L);
        });

        assertEquals("Role not found with ID: 2", exception.getMessage());
    }
}
