package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
  @Autowired
  private CustomerRepository customerRepository;

  // Create or Update Customer
  @Override
  public Customer saveCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  // Get Customer by ID
  @Override
  public Optional<Customer> getCustomerById(Integer id) {
    return customerRepository.findById(id);
  }

  // Get all Customers
  @Override
  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  // Delete Customer by ID
  @Override
  public void deleteCustomerById(Integer id) {
    customerRepository.deleteById(id);
  }

  // Check if Customer exists by ID
  @Override
  public boolean customerExists(Integer id) {
    return customerRepository.existsById(id);
  }

  // Count total Customers
  @Override
  public long getCustomerCount() {
    return customerRepository.count();
  }
}