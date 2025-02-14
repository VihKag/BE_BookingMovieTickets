package com.nvk.cinemav.security.jwt;

import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
     User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
     return new CustomeUserDeails(user);
  }
}
