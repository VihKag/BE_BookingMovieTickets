package com.nvk.cinemav.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService userDetailsService;

  public JwtAuthFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwtToken;
    final String userEmail;

    // Bỏ qua các request OAuth2 và login
    String requestURI = request.getRequestURI();
    if (requestURI.startsWith("/oauth2/") || requestURI.startsWith("/login")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Kiểm tra nếu authHeader không tồn tại hoặc không bắt đầu bằng "Bearer "
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      System.out.println("Không tìm thấy Authorization header hoặc sai format");
      filterChain.doFilter(request, response);
      return;
    }

    jwtToken = authHeader.substring(7);
    userEmail = jwtUtils.extractUsername(jwtToken);
    System.out.println("Extracted Email: " + userEmail);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
      System.out.println("UserDetails: " + userDetails.getUsername());

      if (jwtUtils.isTokenValid(jwtToken)) {
        System.out.println("Token hợp lệ, tiến hành xác thực...");

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);
      } else {
        System.out.println("Token không hợp lệ");
      }
    }
    filterChain.doFilter(request, response);
  }
}
