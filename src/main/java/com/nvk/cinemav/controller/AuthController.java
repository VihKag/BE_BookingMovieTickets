package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.UserDTO;
import com.nvk.cinemav.entity.Role;
import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.repository.RoleRepository;
import com.nvk.cinemav.repository.UserRepository;
import com.nvk.cinemav.response.ApiResponse;
import com.nvk.cinemav.response.AuthResponse;
import com.nvk.cinemav.security.jwt.CustomeUserDeails;
import com.nvk.cinemav.security.jwt.JwtUtils;
import com.nvk.cinemav.security.oauth2.CustomOAuth2UserService;
import com.nvk.cinemav.service.IUserService;
import com.nvk.cinemav.service.impl.UserService;
import com.nvk.cinemav.type.AuthenProvider;
import com.nvk.cinemav.type.UserRole;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final AuthenticationManager authenticationManager;
  private final IUserService userService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;

  public AuthController(AuthenticationManager authenticationManager, UserService userService, UserRepository userRepository,
      PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtils = jwtUtils;
    this.roleRepository = roleRepository;
  }

  @PostMapping("/register")
  @Transactional
  public ResponseEntity<?> register(@RequestBody UserDTO user) {
    if (userService.findUserByEmail(user.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body("Username is already taken.");
    }
    // Map UserDTO to User entity
    User u = new User();
    u.setName(user.getName());
    u.setEmail(user.getEmail());
    u.setPhone(user.getPhone());
    u.setAuthenProvider(AuthenProvider.SYSTEM);
    u.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
    u.setStatus(true); // Set default status
    // Tìm vai trò "USER" trong cơ sở dữ liệu
    Role role = roleRepository.findRolesByRoleName(UserRole.USER)
        .orElseGet(() -> {
          // Nếu không tìm thấy, tạo mới vai trò
          Role newRole = new Role(UserRole.USER);
          roleRepository.save(newRole);
          return newRole;
        });


    u.setRoles(Collections.singletonList(role));
    userRepository.save(u);
    System.out.println("Create account "+ u +" successfully");
    return ResponseEntity.ok("User registered successfully.");
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody UserDTO request) {
    User user = userService.findUserByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("User not found."));
    if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body(null);
    }
    CustomeUserDeails u = new CustomeUserDeails(user);
    String token = jwtUtils.generateToken(u,false);

    AuthResponse response = new AuthResponse(token, u.getUser().getEmail(),u.getUser().getPhone(), u.getUser().getName());
    return ResponseEntity.ok(response);
  }
  @PostMapping("/refresh")
  public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
    try {
      if (!jwtUtils.isTokenValid(refreshToken)) {
        return ResponseEntity.status(401).body(new ApiResponse<>("Invalid Refresh Token"));
      }
      String email = jwtUtils.getEmailFromToken(refreshToken);
      User user = userService.findUserByEmail(email).orElseThrow(()-> new RuntimeException("User not found."));
      CustomeUserDeails u = new CustomeUserDeails(user);
      String newAccessToken = jwtUtils.generateToken(u, true);
      return ResponseEntity.ok(new ApiResponse<>("New access token!",newAccessToken));
    } catch (Exception e) {
      return ResponseEntity.status(401).body(new ApiResponse<>("Invalid Refresh Token"));
    }
  }
  @GetMapping("/oauth2/success")
  public ResponseEntity<?> successLogin(Principal principal) {
    if(principal instanceof OAuth2AuthenticationToken auth2AuthenticationToken) {

      OAuth2User oauth2User = auth2AuthenticationToken.getPrincipal();
      String email = oauth2User.getAttribute("email");
      String name = oauth2User.getAttribute("name");
      logger.info("Login successfully!");
      User user = userService.findUserByEmail(email).orElseThrow(()-> new RuntimeException("User not found."));
      CustomeUserDeails u = new CustomeUserDeails(user);
      String token = jwtUtils.generateToken(u, false);
      return ResponseEntity.ok(new ApiResponse<>("Login successfully!",token));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
  }

  @GetMapping("/oauth2/failure")
  public ResponseEntity<String> failureLogin() {
    return ResponseEntity.status(401).body("Login failed!!");
  }

}
