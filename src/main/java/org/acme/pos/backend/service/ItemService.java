package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ItemService {
  // Create or Update Item
  Item saveItem(Item Item);

  // Get Item by ID
  Optional<Item> getItemById(Integer id);

  // Get all Items
  List<Item> getAllItems();

  // Delete Item by ID
  void deleteItemById(Integer id);

  // Check if Item exists by ID
  boolean ItemExists(Integer id);

  // Count total Items
  long getItemCount();

  List<Item> findByFilter(String sSearchTerm);

}