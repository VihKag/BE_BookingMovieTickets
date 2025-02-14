package com.nvk.cinemav.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
  private String token;
  private String email;
  private String phone;
  private String name;
  public AuthResponse(String token, String email) {
    this.token = token;
    this.email = email;
  }
}
