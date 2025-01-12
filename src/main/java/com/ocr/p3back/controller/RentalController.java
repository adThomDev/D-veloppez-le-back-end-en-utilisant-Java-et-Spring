package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.message.MessageResponse;
import com.ocr.p3back.model.dto.rental.CreateRentalDTO;
import com.ocr.p3back.model.dto.rental.RentalDTO;
import com.ocr.p3back.model.dto.rental.RentalsDTO;
import com.ocr.p3back.service.RentalService;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

  @Autowired
  private RentalService rentalService;

  @Autowired
  private UserService userService;

  @Autowired
  private JwtService jwtService;

  /**
   * Fetches and returns all rental listings.
   *
   * @return ResponseEntity<RentalsDTO> containing a list of {@link RentalDTO} objects representing all rentals.
   */
  @GetMapping
  @Operation(description = "Get all rentals", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalsDTO.class),
          examples = @ExampleObject(value = "{\"rentals\":[{\"id\":1,\"name\":\"Rental 1\",\"surface\":100,\"price\":1000,\"picture\":\"https://example.com/picture1.jpg\",\"description\":\"Description 1\",\"ownerId\":1,\"created_at\":\"2024-01-01\",\"updated_at\":\"2024-01-01\"},{\"id\":2,\"name\":\"Rental 2\",\"surface\":200,\"price\":2000,\"picture\":\"https://example.com/picture2.jpg\",\"description\":\"Description 2\",\"ownerId\":2,\"created_at\":\"2024-01-02\",\"updated_at\":\"2024-01-02\"}]}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content()})
  }, security = {@SecurityRequirement(name = "bearerAuth")})
  public ResponseEntity<RentalsDTO> getAllRentals() {
    return rentalService.getAllRentals();
  }

  /**
   * Gets a specific rental by its ID.
   *
   * @param rentalId The ID of the rental looked for.
   * @return ResponseEntity<RentalDTO> containing the {@link RentalDTO} if found,
   * or a 401 Unauthorized status if not found.
   */
  @GetMapping("/{rentalId}")
  @Operation(description = "Get a rental by ID", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalDTO.class),
          examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Rental 1\",\"surface\":100,\"price\":1000,\"picture\":\"https://example.com/picture1.jpg\",\"description\":\"Description 1\",\"ownerId\":1,\"created_at\":\"2024-01-01\",\"updated_at\":\"2024-01-01\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content()})
  }, security = {@SecurityRequirement(name = "bearerAuth")})
  public ResponseEntity<RentalDTO> getRentalById(@PathVariable(required = true) Long rentalId) {
    return rentalService.getRentalDTOById(rentalId);
  }

  /**
   * Creates a new rental.
   *
   * @param createRentalDTO The details of the new rental.
   * @param authToken       The authentication token of the user creating the rental.
   *                        ResponseEntity containing a {@link MessageResponse} with a success message upon successful creation,
   *                        or a 401 Unauthorized status if user is not authorized or an error occurs.
   */
  @PostMapping
  @Operation(description = "Create a new rental", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = MessageResponse.class),
          examples = @ExampleObject(value = "{\"message\":\"Rental created !\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401"),
  },
      security = {@SecurityRequirement(name = "bearerAuth")},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Rental creation request",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CreateRentalDTO.class)
          )
      )
  )
  public ResponseEntity<MessageResponse> createRental(
      @ModelAttribute CreateRentalDTO createRentalDTO,
      @RequestHeader("Authorization") String authToken) {
    return rentalService.createRental(createRentalDTO, authToken);
  }

  /**
   * Updates an existing rental.
   *
   * @param rentalId        The ID of the rental to update.
   * @param createRentalDTO A {@link CreateRentalDTO} object containing the updated rental details.
   * @param token           The authentication token of the user updating the rental.
   * @return ResponseEntity containing a {@link MessageResponse} with a success message upon successful update,
   * or a 401 Unauthorized status if user is not authorized or there's nothing to update.
   */
  @PutMapping("/{rentalId}")
  @Operation(description = "Update a rental", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = MessageResponse.class),
          examples = @ExampleObject(value = "{\"message\":\"Rental updated !\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401"),
  },
      security = {@SecurityRequirement(name = "bearerAuth")},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "Rental update request",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = CreateRentalDTO.class)
          )
      )
  )
  public ResponseEntity<MessageResponse> updateRental(
      @PathVariable Long rentalId,
      @ModelAttribute CreateRentalDTO createRentalDTO,
      @RequestHeader("Authorization") String token) {
    return rentalService.updateRental(rentalId, createRentalDTO, token);
  }
}