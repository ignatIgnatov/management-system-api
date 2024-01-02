package com.system.management.security.jwt;

import com.system.management.exception.security.InvalidSecretKeyException;
import com.system.management.exception.security.jwt.JwtExpiredException;
import com.system.management.exception.security.jwt.JwtIsNotValidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

  @Value("${jwt.secret.key}")
  private String secretKey;

  @Value("${jwt.token.expiration}")
  private Long tokenExpiration;

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusMillis(tokenExpiration)))
        .signWith(getSigningKey())
        .compact();
  }

  public String generateToken(UserDetails userDetails) {
    if (userDetails == null) {
      throw new BadCredentialsException("UserDetails cannot be null");
    }
    return generateToken(new HashMap<>(), userDetails);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    isNameValid(token, userDetails);
    isTokenExpired(token);
    extractAllClaims(token);
    return true;
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser()
          .decryptWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      throw new JwtExpiredException();
    } catch (JwtException e) {
      throw new JwtIsNotValidException(e);
    }
  }

  public String extractName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractUserEmail(String token) {
    String userEmail = extractName(token);
    if (userEmail == null || userEmail.trim().isEmpty()) {
      throw new JwtIsNotValidException();
    }
    return userEmail;
  }

  private SecretKey getSigningKey() {
    try {
      byte[] keyBytes = Decoders.BASE64.decode(secretKey);
      return Keys.hmacShaKeyFor(keyBytes);
    } catch (WeakKeyException e) {
      throw new InvalidSecretKeyException(e);
    }
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private void isNameValid(String token, UserDetails userDetails) {
    final String name = extractName(token);
    if (!name.equals(userDetails.getUsername())) {
      throw new JwtIsNotValidException();
    }
  }

  private void isTokenExpired(String token) {
    if (extractExpiration(token).isBefore(Instant.now())) {
      throw new JwtExpiredException();
    }
  }

  private Instant extractExpiration(String token) {
    Date expirationDate = extractClaim(token, Claims::getExpiration);
    if (expirationDate == null) {
      throw new JwtExpiredException();
    }
    return expirationDate.toInstant();
  }
}
