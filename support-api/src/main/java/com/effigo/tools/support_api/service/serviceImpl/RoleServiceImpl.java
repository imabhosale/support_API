package com.effigo.tools.support_api.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effigo.tools.support_api.dto.RoleDTO;
import com.effigo.tools.support_api.exception.ResourceNotFoundException;
import com.effigo.tools.support_api.model.Role;
import com.effigo.tools.support_api.repository.RoleRepository;
import com.effigo.tools.support_api.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role savedRole = roleRepository.save(role);
        return modelMapper.map(savedRole, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                             .stream()
                             .map(role -> modelMapper.map(role, RoleDTO.class))
                             .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        
        modelMapper.map(roleDTO, existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }
}
