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
import java.util.Date;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService; // Service pour la gestion des JWT

  @Autowired
  private UserDetailsService userDetailsService; // Service pour la gestion des détails des utilisateurs

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return request.getRequestURI().equals("/pictures")
        || request.getRequestURI().equals("/api/auth/login");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      // Vérifie si l'utilisateur n'est pas déjà authentifié
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        // Récupère l'en-tête "Authorization" et vérifie s'il commence par "Bearer"
        final String token = authHeader.substring(7); // Extrait le token JWT en retirant "Bearer "
        final Claims claims = jwtService.getClaims(token); // Analyse le JWT pour obtenir ses revendications
        if (claims.getExpiration().after(new Date())) {
          // Vérifie si le JWT n'a pas expiré en comparant avec la date actuelle
          final String username = claims.getSubject(); // Récupère le nom d'utilisateur à partir du JWT
          final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          // Charge les détails de l'utilisateur à partir du service UserDetailsService
          final UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          // Crée un objet d'authentification UsernamePasswordAuthenticationToken
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          // Ajoute les détails de l'authentification basés sur la requête
          SecurityContextHolder.getContext().setAuthentication(authToken);
          // Définit l'authentification dans le contexte de sécurité
        }
      }
    }
    filterChain.doFilter(request, response); // Poursuit la chaîne de filtres de sécurité
  }
}