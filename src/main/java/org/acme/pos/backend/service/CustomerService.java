package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface CustomerService {
  // Create or Update Customer
  Customer saveCustomer(Customer customer);

  // Get Customer by ID
  Optional<Customer> getCustomerById(Integer id);

  // Get all Customers
  List<Customer> getAllCustomers();

  // Delete Customer by ID
  void deleteCustomerById(Integer id);

  // Check if Customer exists by ID
  boolean customerExists(Integer id);

  // Count total Customers
  long getCustomerCount();
}
