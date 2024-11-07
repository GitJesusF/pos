package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.OrderItem;
import org.acme.pos.backend.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService{
  @Autowired
  private OrderItemRepository orderItemRepository;

  // Create or Update OrderItem
  @Override
  public OrderItem saveOrderItem(OrderItem OrderItem) {
    return orderItemRepository.save(OrderItem);
  }

  // Get OrderItem by ID
  @Override
  public Optional<OrderItem> getOrderItemById(Integer id) {
    return orderItemRepository.findById(id);
  }

  // Get all OrderItems
  @Override
  public List<OrderItem> getAllOrderItems() {
    return orderItemRepository.findAll();
  }

  // Delete OrderItem by ID
  @Override
  public void deleteOrderItemById(Integer id) {
    orderItemRepository.deleteById(id);
  }

  // Check if OrderItem exists by ID
  @Override
  public boolean OrderItemExists(Integer id) {
    return orderItemRepository.existsById(id);
  }

  // Count total OrderItems
  @Override
  public long getOrderItemCount() {
    return orderItemRepository.count();
  }
}
