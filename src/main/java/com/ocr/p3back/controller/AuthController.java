package com.ocr.p3back.controller;

import com.ocr.p3back.model.ErrorResponse;
import com.ocr.p3back.model.dto.UserDTO;
import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/login")
  @Operation(description = "Authenticate a user", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = AuthResponseDTO.class),
          examples = @ExampleObject(value = "{\"token\":\"jwt\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponse.class),
          examples = @ExampleObject(value = "{\"message\":\"error\"}")
      ))
  },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Authentication request",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AuthRequestDTO.class)
          )
      )
  )
  public ResponseEntity<?> authenticate(@RequestBody AuthRequestDTO authRequestDTO) {
    try {

      return authService.authenticate(authRequestDTO);
    } catch (BadCredentialsException e) {

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("error"));
    }
  }

  @GetMapping("/me")
  @Operation(description = "Get the current user", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserDTO.class),
          examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Bobby Bob\",\"email\":\"bob@example.com\",\"createdAt\":\"2024-01-01\",\"updatedAt\":\"2024-01-01\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401")
  },
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {

    return userService.getCurrentUser(request);
  }
}

