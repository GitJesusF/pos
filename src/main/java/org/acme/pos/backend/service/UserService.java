package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserService {
  // Create or Update User
  User saveUser(User User);

  // Get User by ID
  Optional<User> getUserById(Integer id);

  // Get all Users
  Page<User> getAllUsers(Pageable pageable);

  // Delete User by ID
  void deleteUserById(Integer id);

  // Check if User exists by ID
  boolean UserExists(Integer id);

  // Count total Users
  long getUserCount();

  Page<User> findByFilter(String sSearchTerm, Pageable pageable);
}
