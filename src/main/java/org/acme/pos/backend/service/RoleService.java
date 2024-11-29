package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RoleService {
  // Create or Update Role
  Role saveRole(Role role);

  // Get Role by ID
  Optional<Role> getRoleById(Integer id);

  // Get all Roles
  Page<Role> getAllRoles(Pageable pageable);

  // Delete Role by ID
  void deleteRoleById(Integer id);

  // Check if Role exists by ID
  boolean RoleExists(Integer id);

  // Count total Roles
  long getRoleCount();

  Page<Role> findByFilter(String sSearchTerm, Pageable pageable);
}