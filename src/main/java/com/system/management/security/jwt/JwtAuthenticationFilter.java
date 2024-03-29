package com.system.management.security.jwt;

import com.system.management.exception.security.jwt.JwtTokenNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";
  private static final int TOKEN_OFFSET = TOKEN_PREFIX.length();

  private final JwtUtils jwtUtils;
  private final UserDetailsService userDetailsService;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  public JwtAuthenticationFilter(
      JwtUtils jwtUtils,
      UserDetailsService userDetailsService,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    if (isUnprotectedEndpoint(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      authenticateRequest(request);
    } catch (AuthenticationException e) {
      clearSecurityContextAndCommenceEntryPoint(request, response, e);
      return;
    }
    filterChain.doFilter(request, response);
  }

  private boolean isUnprotectedEndpoint(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    return requestURI.startsWith("/auth/login") || requestURI.startsWith("/auth/register");
  }

  private void authenticateRequest(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION_HEADER);
    String token = validateJwtHeader(authHeader);
    String userEmail = jwtUtils.extractUserEmail(token);

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
      if (jwtUtils.isTokenValid(token, userDetails)) {
        setAuthenticationContext(request, userDetails);
      }
    }
  }

  private String validateJwtHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
      throw new JwtTokenNotFoundException();
    }
    return authHeader.substring(TOKEN_OFFSET);
  }

  private void setAuthenticationContext(HttpServletRequest request, UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  private void clearSecurityContextAndCommenceEntryPoint(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
    SecurityContextHolder.clearContext();
    jwtAuthenticationEntryPoint.commence(request, response, e);
  }
}
