package org.acme.pos.backend.repository;

import org.acme.pos.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  @Query("SELECT r FROM Role r " +
      "WHERE LOWER(r.name) LIKE %?1% ")
  List<Role> findByFilter(String sSearchTerm);

}
