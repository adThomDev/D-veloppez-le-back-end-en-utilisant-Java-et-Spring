package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.ErrorResponse;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticate(@RequestBody AuthRequestDTO authRequestDTO) {
    try {
      return authService.authenticate(authRequestDTO);
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Invalid credentials"));
    }
  }

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
    return userService.getCurrentUser(request);
  }
}

