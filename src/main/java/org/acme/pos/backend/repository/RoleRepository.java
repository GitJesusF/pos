package org.acme.pos.backend.repository;

import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  @Query("SELECT r FROM Role r " +
      "WHERE LOWER(r.name) LIKE %?1% ")
  Page<Role> findByFilter(String sSearchTerm, Pageable sort);
}
