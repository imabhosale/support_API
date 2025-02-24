package com.effigo.tools.support_api.service;


import java.util.List;
import com.effigo.tools.support_api.dto.RoleDTO;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Long id);
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
}
