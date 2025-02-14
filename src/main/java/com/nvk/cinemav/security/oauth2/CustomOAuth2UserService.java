package com.nvk.cinemav.security.oauth2;

import com.nvk.cinemav.dto.UserDTO;
import com.nvk.cinemav.entity.Role;
import com.nvk.cinemav.entity.User;
import com.nvk.cinemav.repository.RoleRepository;
import com.nvk.cinemav.repository.UserRepository;
import com.nvk.cinemav.type.AuthenProvider;
import com.nvk.cinemav.type.UserRole;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @PostConstruct
  public void init() {
    log.info("✅ CustomOAuth2UserService đã được khởi tạo!");
  }
  @Override
  @SneakyThrows
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    log.trace("Loading user for request: {}", oAuth2UserRequest);

    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    return processOAuth2User(oAuth2UserRequest, oAuth2User);
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String name = (String) attributes.getOrDefault("name", "Unknown");
    String id = (String) attributes.getOrDefault("sub", "No-ID");
    String email = (String) attributes.getOrDefault("email", "No-Email");
    String phone = (String) attributes.getOrDefault("phone", "No-Phone");
//    String picture = (String) attributes.getOrDefault("picture", "");

    UserDTO userInfoDto = UserDTO.builder()
        .name(name)
        .email(email)
        .phone(phone)
        .build();

    log.trace("Extracted user info: {}", userInfoDto);

    return userRepository.findUserByEmail(email)
        .map(existingUser -> updateExistingUser(existingUser, userInfoDto))
        .map(user -> CustomeUserOauth2.create(user, attributes))
        .orElseGet(() -> {
          User newUser = registerNewUser(oAuth2UserRequest, userInfoDto);
          return CustomeUserOauth2.create(newUser, attributes);
        });
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, UserDTO userInfoDto) {
    User user = new User();
    user.setStatus(true);
    user.setAuthenProvider(AuthenProvider.GOOGLE);
    // Tìm vai trò "USER" trong cơ sở dữ liệu
    Role role = roleRepository.findRolesByRoleName(UserRole.USER)
        .orElseGet(() -> {
          // Nếu không tìm thấy, tạo mới vai trò
          Role newRole = new Role(UserRole.USER);
          roleRepository.save(newRole);
          return newRole;
        });
    user.setRoles(Collections.singletonList(role));
    user.setName(userInfoDto.getName());
    user.setEmail(userInfoDto.getEmail());
    user.setPhone(userInfoDto.getPhone());
    log.info("Registering new user: {}", user);
    return userRepository.save(user);
  }

  private User updateExistingUser(User existingUser, UserDTO userInfoDto) {
    existingUser.setName(userInfoDto.getName());
    existingUser.setPhone(userInfoDto.getPhone());
    existingUser.setAuthenProvider(AuthenProvider.GOOGLE);
    log.info("Updating existing user: {}", existingUser);
    return userRepository.save(existingUser);
  }


}
