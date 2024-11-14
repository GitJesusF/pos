package org.acme.pos.backend.repository;

import org.acme.pos.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

  @Query("SELECT i FROM Item i " +
      "WHERE LOWER(i.code) LIKE %?1% " +
      "OR LOWER(i.name) LIKE %?1% ")
  List<Item> findByFilter(String sSearchTerm);
}
