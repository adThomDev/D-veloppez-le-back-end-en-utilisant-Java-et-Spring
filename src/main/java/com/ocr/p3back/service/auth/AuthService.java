package com.ocr.p3back.service.auth;

import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.model.dto.message.MessageResponse;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  /**
   * Verifies login information and provides access or an error message.
   *
   * @param authRequestDTO The provided email and password.
   * @return Upon successful authentication, a token is provided. Otherwise, an error message is returned.
   */
  public ResponseEntity<?> authenticate(AuthRequestDTO authRequestDTO) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authRequestDTO.getEmail(), authRequestDTO.getPassword()));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("error"));
    }

    UserEntity userEntity = userService.findUserByEmail(authRequestDTO.getEmail());
    AuthResponseDTO authResponseDTO = new AuthResponseDTO(jwtService.generateToken(userEntity.getEmail()));

    if (userEntity == null || authResponseDTO == null) {  //TODO
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("error"));
    }

    return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
  }
}

