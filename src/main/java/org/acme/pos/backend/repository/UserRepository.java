package org.acme.pos.backend.repository;

import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM User u " +
      "JOIN u.role r " +
      "WHERE LOWER(u.firstName) LIKE %?1% " +
      "OR LOWER(u.lastName) LIKE %?1% " +
      "OR LOWER(u.username) LIKE %?1% " +
      "OR LOWER(u.email) LIKE %?1% " +
      "OR LOWER(r.name) LIKE %?1%")
  Page<User> findByFilter(String sSearchTerm, Pageable sort);
}