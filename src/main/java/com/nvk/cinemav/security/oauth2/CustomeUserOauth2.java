package com.nvk.cinemav.security.oauth2;

import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.type.UserRole;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomeUserOauth2 implements OAuth2User {
  private final String id;
  private final String name;
  private final String email;
  private final String phone;
  private final Collection<? extends GrantedAuthority> authorities;
  private final Map<String, Object> attributes;

  public CustomeUserOauth2(String id, String name, String email, String phone,
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.authorities = authorities;
    this.attributes = attributes;
  }

  // 🛠 Thêm phương thức create() vào đây
  public static CustomeUserOauth2 create(User user, Map<String, Object> attributes) {
    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(UserRole.USER.toString()));

    return new CustomeUserOauth2(
        user.getId().toString(),  // Chuyển UUID thành String
        user.getName(),
        user.getEmail(),
        user.getPhone(),
        authorities,
        attributes
    );
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getName() {
    return name;
  }
}
