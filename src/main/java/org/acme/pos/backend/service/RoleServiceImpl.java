package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.entity.Role;
import org.acme.pos.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


  public Page<Role> getAllRoles(Pageable pageable) {
    return roleRepository.findAll(pageable);
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

  @Override
  public Page<Role> findByFilter(String sSearchTerm, Pageable pageable) {
    return roleRepository.findByFilter(sSearchTerm, pageable);
  }
}
