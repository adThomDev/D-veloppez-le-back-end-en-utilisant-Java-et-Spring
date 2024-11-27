package com.ocr.p3back.controller;


import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.model.entity.ErrorResponse;
import com.ocr.p3back.service.auth.AuthService;
import com.ocr.p3back.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private JwtService jwtService;

  /**
   * Reçoit identifiant et mot de passe et vérifie leur validité. Renvoie un DTO contenant un message disant si
   * l'authentification est valide ou non.
   *
   * @param authRequestDTO le DTO qui contient les informations d'identification (identifiant et mot de passe).
   */
  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO authRequestDTO) {
    return ResponseEntity.ok(authService.authenticate(authRequestDTO));
  }

//  @GetMapping("/me")
//  public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authorizationHeader) {
//    try {
//      Long userIdFromToken = getUserIdFromAuthorizationHeader(authorizationHeader);
//      UserInfoResponse userEntity = verifyAndGetUserByTokenId(userIdFromToken);
//
//      return ResponseEntity.status(HttpStatus.OK).body(userEntity);
//    } catch (ApiException ex) {
//      return GlobalExceptionHandler.handleApiException(ex);
//    }
//  }
//
//  private Long getUserIdFromAuthorizationHeader(String authorizationHeader) {
//    String jwtToken = JwtUtil.extractJwtFromHeader(authorizationHeader);
//
//    // Extract user ID from JWT
//    Optional<Long> optionalUserIdFromToken = JwtUtil.extractUserId(jwtToken);
//
//    Boolean hasJwtExtractionError = optionalUserIdFromToken.isEmpty();
//    if (hasJwtExtractionError) {
//      GlobalExceptionHandler.handleLogicError("Unauthorized", HttpStatus.UNAUTHORIZED);
//    }
//
//    return optionalUserIdFromToken.get();
//  }

  //pour l'enregistrement de compte :
//  @PostMapping("/register")
//  public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO dto) {
//    return ResponseEntity.ok(authService.register(dto));
//  }
}

