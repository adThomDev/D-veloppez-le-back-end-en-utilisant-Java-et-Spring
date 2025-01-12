package com.ocr.p3back.controller;

import com.ocr.p3back.model.ErrorResponse;
import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.model.dto.user.UserDTO;
import com.ocr.p3back.model.dto.user.UserRegistrationDTO;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @Autowired
  public AuthController(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  /**
   * Checks login details and gives access if they're correct.
   *
   * @param authRequestDTO The email and password used to log in.
   * @return A JWT if login is successful, or an error if not.
   */
  @PostMapping("/login")
  @Operation(description = "Authenticate a user", responses = {
      @ApiResponse(description = "Login successful", responseCode = "200", content = @Content(
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
    return authService.authenticate(authRequestDTO);
  }

  /**
   * Gets the information for the currently logged-in user.
   *
   * @param request The data containing the user's JWT.
   * @return The user's information on success, or an error if not.
   */
  @GetMapping("/me")
  @Operation(description = "Get the current user", responses = {
      @ApiResponse(description = "User info retriaval successful", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserDTO.class),
          examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Bobby Bob\",\"email\":\"bob@example.com\",\"createdAt\":\"2024-01-01\",\"updatedAt\":\"2024-01-01\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401")
  },
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
    return userService.getCurrentUser(request);
  }

  /**
   * Registers a new user.
   *
   * @param userRegistrationDTO The user details for registration encapsulated in a UserDTO object.
   * @return A ResponseEntity containing the new user's JWT if successful, or a 400.
   */
  @PostMapping("/register")
  @Operation(description = "Register a new user", responses = {
      @ApiResponse(description = "User registeration successful", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = AuthResponseDTO.class),
          examples = @ExampleObject(value = "{\"token\":\"generated-jwt-token\"}")
      )),
      @ApiResponse(description = "Bad request", responseCode = "400")
  },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Registration request",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserRegistrationDTO.class)
          )
      )
  )
  public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
    return userService.createUser(userRegistrationDTO);
  }
}

