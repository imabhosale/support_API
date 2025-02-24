package com.effigo.tools.support_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effigo.tools.support_api.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}