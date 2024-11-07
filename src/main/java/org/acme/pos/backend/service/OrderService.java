package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Order;
import org.acme.pos.backend.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface OrderService {
  // Create or Update Order
  Order saveOrder(Order order, List<OrderItem> orderItems);

  // Get Order by ID
  Optional<Order> getOrderById(Integer id);

  // Get all Orders
  List<Order> getAllOrders();

  // Delete Order by ID
  void deleteOrderById(Integer id);

  // Check if Order exists by ID
  boolean OrderExists(Integer id);

  // Count total Orders
  long getOrderCount();
}
