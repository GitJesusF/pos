package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ItemService {
  // Create or Update Item
  Item saveItem(Item Item);

  // Get Item by ID
  Optional<Item> getItemById(Integer id);

  // Get all Items
  Page<Item> getAllItems(Pageable pageable);

  // Delete Item by ID
  void deleteItemById(Integer id);

  // Check if Item exists by ID
  boolean ItemExists(Integer id);

  // Count total Items
  long getItemCount();

  Page<Item> findByFilter(String sSearchTerm, Pageable pageable);

}