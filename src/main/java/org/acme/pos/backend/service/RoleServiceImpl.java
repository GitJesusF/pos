package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Role;
import org.acme.pos.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{
  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role saveRole(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public Optional<Role> getRoleById(Integer id) {
    return roleRepository.findById(id);
  }

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  @Override
  public void deleteRoleById(Integer id) {
    roleRepository.deleteById(id);

  }

  @Override
  public boolean RoleExists(Integer id) {
    return roleRepository.existsById(id);
  }

  @Override
  public long getRoleCount() {
    return roleRepository.count();
  }
}
