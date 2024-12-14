package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.UserDTO;
import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.model.ErrorResponse;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.AuthService;
import com.ocr.p3back.service.auth.JwtService;
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
  private JwtService jwtService;

  @Autowired
  private UserService userService;


  @PostMapping("/login")
  public ResponseEntity<?> authenticate(@RequestBody AuthRequestDTO authRequestDTO) {
    try {
      AuthResponseDTO responseDTO = authService.authenticate(authRequestDTO);
      return ResponseEntity.ok(responseDTO);
//      return ResponseEntity.status(HttpStatus.OK).body(responseDTO); //aussi possible
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Invalid credentials"));
    }
  }

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
    final String authHeader = request.getHeader("Authorization");

    try {
      String token = authHeader.replace("Bearer ", "");
      String email = jwtService.getClaims(token).getSubject();
      UserEntity user = userService.findUserByEmail(email);

      if (user != null) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        return ResponseEntity.ok(userDTO);
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("User not found"));
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Invalid token"));
    }
  }

  //pour l'enregistrement de compte :
//  @PostMapping("/register")
//  public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO dto) {
//    return ResponseEntity.ok(authService.register(dto));
//  }
}

