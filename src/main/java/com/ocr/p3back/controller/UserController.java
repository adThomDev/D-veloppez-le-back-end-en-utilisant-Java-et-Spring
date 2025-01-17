package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.user.UserDTO;
import com.ocr.p3back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Gets the information for a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return A ResponseEntity containing the UserDTO if found, or the appropriate status otherwise.
   */
  @GetMapping("/{id}")
  @Operation(description = "Get user by ID", responses = {
      @ApiResponse(description = "User info retrieval successful", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = UserDTO.class),
          examples = @ExampleObject(value = "{\"id\":2,\"name\":\"Alice Doe\",\"email\":\"alice@example.com\",\"createdAt\":\"2024-01-01\",\"updatedAt\":\"2024-01-01\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401")
  },
      security = {@SecurityRequirement(name = "bearerAuth")}
  )
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    UserDTO userDTO = userService.getUserById(id);

    if (userDTO != null) {
      return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
