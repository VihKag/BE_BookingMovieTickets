package com.nvk.cinemav.controller;

import com.nvk.cinemav.dto.UserDTO;
import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.repository.UserRepository;
import com.nvk.cinemav.response.ApiResponse;
import com.nvk.cinemav.service.IUserService;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/api/users")
public class UserController {

  private final IUserService userService;
  private final UserRepository userRepository;

  public UserController(IUserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  @GetMapping("")
  public ResponseEntity<ApiResponse<?>> getUsers(Pageable pageable) {
    try{
      Page<User> users = userService.findAllUsers(pageable);
      Page<UserDTO> userDTOS = users.map(user -> new UserDTO(
          user.getEmail(),
          user.getName(),
          user.getPhone()
      ));
      ApiResponse<Page<UserDTO>> response = new ApiResponse<>("Get list of users successfully!", userDTOS );
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid request parameters!"));
    }
    catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("Database error occurred!"));
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("An unexpected error occurred!"));
    }
  }
  @PostMapping("/status/{email}")
  public ResponseEntity<ApiResponse<?>> changeStatus(@PathVariable String email){
    try{
      Optional<User> user = userRepository.findUserByEmail(email);
      if(!user.isPresent()){
        return ResponseEntity.badRequest().body(new ApiResponse<>("User not found!"));
      }
      if(user.orElseThrow().isStatus()){
        user.orElseThrow().setStatus(false);
        userRepository.save(user.get());
      }else {
        user.orElseThrow().setStatus(true);
      }
      return ResponseEntity.ok(new ApiResponse<>("User status changed successfully!"));
    }
    catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("Database error occurred!"));
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("An unexpected error occurred!"));
    }
  }

  @PutMapping("/update")
  public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody UserDTO userDTO){
    try{
      User user = userRepository.findUserByEmail(userDTO.getEmail())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
      if (userDTO.getName() != null) {
        user.setName(userDTO.getName());
      }
      if (userDTO.getPhone() != null) {
        user.setPhone(userDTO.getPhone());
      }
      userRepository.save(user);
      return ResponseEntity.ok(new ApiResponse<>("User updated successfully!"));
    }
    catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("Database error occurred!"));
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>("An unexpected error occurred!"));
    }
  }
}
