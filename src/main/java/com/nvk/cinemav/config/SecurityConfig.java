package com.nvk.cinemav.config;

import com.nvk.cinemav.security.jwt.JwtAuthFilter;
import com.nvk.cinemav.security.oauth2.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter;
  private final CustomOAuth2UserService customOAuth2UserService;
  public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomOAuth2UserService customOAuth2UserService) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.customOAuth2UserService = customOAuth2UserService;
  }

  private static final String[] AUTH_WHITELIST = {
      "/api/*",
      "/**",
      "/auth/*",
      "/users/*",
      "/cinemas",
      "/genres/*",
      "/movies",
      "/movies/*",
      "/oauth2/**",
      "/shows/*",
      "/payment/*",
      "/oauth2/authorization/google"
  };
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(corsCustomizer->corsCustomizer.configurationSource(new CorsConfigurationSource() {
          @Override
          public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            CorsConfiguration corsConfiguration=new CorsConfiguration();
            corsConfiguration.setAllowCredentials(true);// allows taking authentication with credentials
            corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
            // providing the allowed origin details, can provide multiple origins here, 8080 is the port number of client application here
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));// allowing all HTTP methods GET,POST,PUT etc, can configure on your need
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));// allowing all the request headers, can configure according to your need, which headers to allow
            corsConfiguration.setMaxAge(Duration.ofMinutes(5L)); // setting the max time till which the allowed origin will not make a pre-flight request again to check if the CORS is allowed on not
            return corsConfiguration;
          }
        }))
        .csrf(AbstractHttpConfigurer::disable)// Tắt CSRF nếu không cần
        .authorizeHttpRequests(auth->auth
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().authenticated())// Cho phép truy cập không cần xác thực cho các URL đăng nhập, đăng ký
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
              response.setContentType("application/json");
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.getWriter().write("{\"error\": \"Unauthorized access\"}");
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permit to access!");
            })
        )
        .oauth2Login(oauth2 -> oauth2
            .defaultSuccessUrl("/auth/oauth2/success", true) // Redirect sau khi đăng nhập thành công
            .failureUrl("/auth/login?error=true") // Redirect khi đăng nhập thất bại
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)) // Service để lấy thông tin người dùng
        )
        .logout(logout -> logout
            .logoutUrl("/auth/logout")
            .logoutSuccessUrl("/auth/login")             // Đăng xuất thành công chuyển về trang đăng nhập chung
            .permitAll()
        )
//        .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Tắt session
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Cho phép session khi cần thiết
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Thêm JwtAuthFilter trước bộ lọc xác thực mặc định
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Mã hóa mật khẩu
  }
  @Bean
  public OAuth2AuthorizedClientService authorizedClientService(
      ClientRegistrationRepository clientRegistrationRepository) {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
  }
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
