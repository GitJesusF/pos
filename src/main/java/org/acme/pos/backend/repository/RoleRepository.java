package org.acme.pos.backend.repository;

import org.acme.pos.backend.entity.Role;
import org.acme.pos.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
