package org.acme.pos.backend.service;

import org.acme.pos.backend.entity.User;
import org.acme.pos.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
  public List<User> getAllUsers() {
    return userRepository.findAll();
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
  public List<User> findByFilter(String sSearchTerm) {
    return userRepository.findByFilter(sSearchTerm);
  }
}
