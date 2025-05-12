package com.nvk.cinemav.entity;

import com.nvk.cinemav.type.AuthenProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;
  @Column(unique = true)
  private String email;
  private String password;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles", // Tên bảng trung gian
      joinColumns = @JoinColumn(name = "user_id"), // Khóa ngoại trỏ tới User
      inverseJoinColumns = @JoinColumn(name = "role_id") // Khóa ngoại trỏ tới Role
  )
  private List<Role> roles = new ArrayList<>(); // Danh sách vai trò của user
  private String phone;
  private boolean status;
  @Enumerated(EnumType.STRING)
  private AuthenProvider authenProvider; // GOOGLE, SYSTEM
}
