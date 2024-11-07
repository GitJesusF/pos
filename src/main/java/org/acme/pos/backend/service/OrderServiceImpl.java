package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Order;
import org.acme.pos.backend.entity.OrderItem;
import org.acme.pos.backend.repository.OrderRepository;
import org.acme.pos.backend.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderItemRepository orderItemRepository;


  // Create or Update Order
  @Override
  public Order saveOrder(Order order, List<OrderItem> orderItems) {
    Order data = orderRepository.save(order);
    orderItemRepository.saveAll(orderItems);
    return data;
  }

  // Get Order by ID
  @Override
  public Optional<Order> getOrderById(Integer id) {
    return orderRepository.findById(id);
  }

  // Get all Orders
  @Override
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  // Delete Order by ID
  @Override
  public void deleteOrderById(Integer id) {
    orderRepository.deleteById(id);
  }

  // Check if Order exists by ID
  @Override
  public boolean OrderExists(Integer id) {
    return orderRepository.existsById(id);
  }

  // Count total Orders
  @Override
  public long getOrderCount() {
    return orderRepository.count();
  }
}
