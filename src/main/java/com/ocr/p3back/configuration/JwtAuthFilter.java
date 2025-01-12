package com.ocr.p3back.configuration;

import com.ocr.p3back.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Autowired
  public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Determines whether the given request should not be filtered.
   *
   * @param request The HttpServletRequest to be checked.
   * @return true if the request URI matches any of the specified paths and should not be filtered, false otherwise.
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getRequestURI().equals("/pictures")
        || request.getRequestURI().equals("/api/auth/login")
        || request.getRequestURI().equals("/api/auth/register");
  }

  /**
   * Performs filtering of incoming HTTP requests to process JWT authentication.
   *
   * @param request     The HttpServletRequest to be processed.
   * @param response    The HttpServletResponse to be sent.
   * @param filterChain The FilterChain that the request will pass through.
   * @throws ServletException If an error occurs during the filtering process.
   * @throws IOException      If an input or output error occurs while handling the request.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);

      return;
    }

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      final String token = authHeader.substring(7);
      final Claims claims = jwtService.getClaims(token);

      if (claims.getExpiration().after(new Date())) {
        final String username = claims.getSubject();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}