package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface OrderItemService {
  // Create or Update OrderItem
  OrderItem saveOrderItem(OrderItem OrderItem);

  // Get OrderItem by ID
  Optional<OrderItem> getOrderItemById(Integer id);

  // Get all OrderItems
  List<OrderItem> getAllOrderItems();

  // Delete OrderItem by ID
  void deleteOrderItemById(Integer id);

  // Check if OrderItem exists by ID
  boolean OrderItemExists(Integer id);

  // Count total OrderItems
  long getOrderItemCount();
}
