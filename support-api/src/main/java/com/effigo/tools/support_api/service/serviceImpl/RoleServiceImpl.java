package com.effigo.tools.support_api.service.serviceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effigo.tools.support_api.dto.RoleDTO;
import com.effigo.tools.support_api.exception.DuplicateResourceException;
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

		if (roleRepository.existsByRoleName(roleDTO.getRoleName())) {
			throw new DuplicateResourceException("Role with name '" + roleDTO.getRoleName() + "' already exists.");
		}

		Role role = modelMapper.map(roleDTO, Role.class);

		if (role.getCreatedBy() == null || role.getCreatedBy().isEmpty()) {
			role.setCreatedBy("System");
		}

		role.setCreatedOn(new Timestamp(System.currentTimeMillis()));

		Role savedRole = roleRepository.save(role);
		return modelMapper.map(savedRole, RoleDTO.class);
	}

	@Override
	public List<RoleDTO> getAllRoles() {
		return roleRepository.findAll().stream().map(role -> modelMapper.map(role, RoleDTO.class))
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

		if (!existingRole.getRoleName().equals(roleDTO.getRoleName())
				&& roleRepository.existsByRoleName(roleDTO.getRoleName())) {
			throw new DuplicateResourceException("Role with name '" + roleDTO.getRoleName() + "' already exists.");
		}

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
