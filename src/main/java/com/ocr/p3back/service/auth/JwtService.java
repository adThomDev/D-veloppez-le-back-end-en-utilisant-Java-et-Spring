package com.ocr.p3back.service.auth;

import com.ocr.p3back.dao.UserRepository;
import com.ocr.p3back.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

  private final UserRepository utilisateurRepository;
  private final String secretKey;
  private final Long expiration;

  @Autowired
  public JwtService(UserRepository utilisateurRepository,
                    @Value("${jwt.secret.key}") String secretKey,
                    @Value("${jwt.expiration}") Long expiration) {
    this.utilisateurRepository = utilisateurRepository;
    this.secretKey = secretKey;
    this.expiration = expiration;
  }


  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  /**
   * Generates a JSON Web Token for the given username.
   *
   * @param username The username for which the token is generated.
   * @return A JWT token as a String, or null if the user is not found.
   */
  public String generateToken(String username) {
    UserEntity utilisateur = utilisateurRepository.findByEmail(username).orElse(null);
    Map<String, Object> claims = new HashMap<String, Object>();

    return Jwts.builder()
        .setSubject(username)
        .addClaims(claims)
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  public Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
  }

  public String extractUsername(String token) {
    Claims claims = getClaims(token);

    return claims.getSubject();
  }
}