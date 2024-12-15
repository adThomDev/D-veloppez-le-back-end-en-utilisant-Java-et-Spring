package com.ocr.p3back.service.auth;

import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  public ResponseEntity<?> authenticate(AuthRequestDTO authRequestDTO) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              authRequestDTO.getEmail(), authRequestDTO.getPassword()));
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid credentials", e);
    }

    UserEntity userEntity = userService.findUserByEmail(authRequestDTO.getEmail());
    return ResponseEntity.ok(new AuthResponseDTO(jwtService.generateToken(userEntity.getEmail())));
  }
}

