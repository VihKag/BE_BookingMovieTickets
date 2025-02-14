package com.nvk.cinemav.security.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
  @Value("${JWT_SECRET_KEY}")
  private String secretKeyString;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
  }

  @Value("${ACCESS_TOKEN_EXPIRY}")
  private Long ACCESS_TOKEN_EXPIRY;
  @Value("${REFRESH_TOKEN_EXPIRY}")
  private Long REFRESH_TOKEN_EXPIRY;

  public String generateToken(CustomeUserDeails userDeails, boolean isRefresh) {
    long expiry = isRefresh ? REFRESH_TOKEN_EXPIRY : ACCESS_TOKEN_EXPIRY;
    return Jwts.builder()
        .setSubject(userDeails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis()+ expiry))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }
  public String getEmailFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
  public String extractUsername(String token){

    return extractClaims(token, Claims::getSubject);
  }
  private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
    try {
      Claims claims = Jwts.parser()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
      return claimsTFunction.apply(claims);
    } catch (JwtException e) {
      throw new RuntimeException("Invalid JWT Token", e);
    }
  }
  public boolean isTokenValid(String token){
    try {
      Jwts.parser()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException ex) {
      return false;
    }
  }
  public boolean isTokenExpired(String token){
    return extractClaims(token, Claims::getExpiration).before(new Date());
  }
}
