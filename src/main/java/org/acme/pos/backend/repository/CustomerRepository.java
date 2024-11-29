package org.acme.pos.backend.repository;

import org.acme.pos.backend.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

  @Query("SELECT c FROM Customer c " +
      "WHERE LOWER(c.firstName) LIKE %?1% " +
      "OR LOWER(c.lastName) LIKE %?1% " +
      "OR LOWER(c.phone) LIKE %?1% " +
      "OR LOWER(c.email) LIKE %?1% ")
  Page<Customer> findByFilter(String sSearchTerm, Pageable sort);
}