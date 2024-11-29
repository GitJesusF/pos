package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.Customer;
import org.acme.pos.backend.entity.User;
import org.acme.pos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  private UserRepository userRepository;

  // Create or Update User
  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  // Get User by ID
  @Override
  public Optional<User> getUserById(Integer id) {
    return userRepository.findById(id);
  }

  // Get all Users
  @Override
  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  // Delete User by ID
  @Override
  public void deleteUserById(Integer id) {
    userRepository.deleteById(id);
  }

  // Check if User exists by ID
  @Override
  public boolean UserExists(Integer id) {
    return userRepository.existsById(id);
  }

  // Count total Users
  @Override
  public long getUserCount() {
    return userRepository.count();
  }

  @Override
  public Page<User> findByFilter(String sSearchTerm, Pageable pageable) {
    return userRepository.findByFilter(sSearchTerm, pageable);
  }
}
