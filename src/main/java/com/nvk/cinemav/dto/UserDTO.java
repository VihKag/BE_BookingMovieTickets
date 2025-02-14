package com.nvk.cinemav.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  private String email;
  private String password;
  private String name;
  private String phone;

  public UserDTO(String email, String name, String phone) {
    this.email = email;
    this.name = name;
    this.phone = phone;
  }
  //Getter Setter

}
