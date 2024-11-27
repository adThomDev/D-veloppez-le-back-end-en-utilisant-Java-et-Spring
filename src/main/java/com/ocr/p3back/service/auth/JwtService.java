package com.ocr.p3back.service.auth;

import com.ocr.p3back.dao.UserRepository;
import com.ocr.p3back.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${jwt.secret.key}")
  private String secretKey; // Clé secrète utilisée pour signer le JWT

  @Value("${jwt.expiration}")
  private Long expiration; // Durée de validité du JWT (en millisecondes)

  @Autowired
  private UserRepository utilisateurRepository; // Repository pour accéder aux données des utilisateurs

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes()); // Obtient la clé secrète au format SecretKey
  }

  public String generateToken(String username) {
    // Méthode pour générer un JWT

    UserEntity utilisateur = utilisateurRepository.findByEmail(username).orElseThrow();
    // Récupère les informations de l'utilisateur à partir du repository

    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("role", utilisateur.getClass().getSimpleName());
    // Crée des revendications (claims) personnalisées pour le JWT, telles que le rôle de l'utilisateur

    return Jwts.builder()
        .setSubject(username)
        .addClaims(claims)
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getKey(), SignatureAlgorithm.HS512)
        .compact();
    // Construit le JWT avec les revendications, la date d'expiration, et le signe avec la clé secrète
  }

  public Claims getClaims(String token) {
    // Méthode pour extraire les revendications d'un JWT

    return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    // Utilise la clé secrète pour vérifier et extraire les revendications du JWT
  }
}

