package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Item;
import org.acme.pos.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService{
  @Autowired
  private ItemRepository itemRepository;

  // Create or Update Item
  @Override
  public Item saveItem(Item Item) {
    return itemRepository.save(Item);
  }

  // Get Item by ID
  @Override
  public Optional<Item> getItemById(Integer id) {
    return itemRepository.findById(id);
  }

  // Get all Items
  @Override
  public List<Item> getAllItems() {
    return itemRepository.findAll();
  }

  // Delete Item by ID
  @Override
  public void deleteItemById(Integer id) {
    itemRepository.deleteById(id);
  }

  // Check if Item exists by ID
  @Override
  public boolean ItemExists(Integer id) {
    return itemRepository.existsById(id);
  }

  // Count total Items
  @Override
  public long getItemCount() {
    return itemRepository.count();
  }
}