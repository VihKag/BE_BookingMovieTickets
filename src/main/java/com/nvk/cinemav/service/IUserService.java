package com.nvk.cinemav.service;

import com.nvk.cinemav.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
  Optional<User> findUserByEmail(String email);
  Page<User> findAllUsers(Pageable pageable);
}
