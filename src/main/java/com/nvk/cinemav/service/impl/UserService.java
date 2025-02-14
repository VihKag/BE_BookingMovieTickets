package com.nvk.cinemav.service.impl;

import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.repository.UserRepository;
import com.nvk.cinemav.service.IUserService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
  private final UserRepository userRepository;
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Override
  public Page<User> findAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }
}
