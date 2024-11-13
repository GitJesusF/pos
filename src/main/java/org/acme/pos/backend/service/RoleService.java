package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface RoleService {
  // Create or Update Role
  Role saveRole(Role role);

  // Get Role by ID
  Optional<Role> getRoleById(Integer id);

  // Get all Roles
  List<Role> getAllRoles();

  // Delete Role by ID
  void deleteRoleById(Integer id);

  // Check if Role exists by ID
  boolean RoleExists(Integer id);

  // Count total Roles
  long getRoleCount();
}
