package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.CreateRentalDTO;
import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.dto.RentalsDTO;
import com.ocr.p3back.model.dto.message.MessageResponse;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.RentalService;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

  private final RentalService rentalService;
  private final UserService userService;
  private final JwtService jwtService;

  @GetMapping
  @Operation(description = "Get all rentals", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalsDTO.class),
          examples = @ExampleObject(value = "{\"rentals\":[{\"id\":1,\"name\":\"Rental 1\",\"surface\":100,\"price\":1000,\"picture\":\"https://example.com/picture1.jpg\",\"description\":\"Description 1\",\"ownerId\":1,\"created_at\":\"2022-01-01\",\"updated_at\":\"2022-01-01\"},{\"id\":2,\"name\":\"Rental 2\",\"surface\":200,\"price\":2000,\"picture\":\"https://example.com/picture2.jpg\",\"description\":\"Description 2\",\"ownerId\":2,\"created_at\":\"2022-01-02\",\"updated_at\":\"2022-01-02\"}]}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content()})
  }, security = {@SecurityRequirement(name = "bearerAuth")})
  public ResponseEntity<RentalsDTO> getAllRentals() {
    RentalsDTO rentalsDTO = new RentalsDTO(rentalService.getAllRentals());

    return new ResponseEntity<>(rentalsDTO, HttpStatus.OK);
  }

  @GetMapping("/{rentalId}")
  @Operation(description = "Get a rental by ID", responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RentalDTO.class),
          examples = @ExampleObject(value = "{\"id\":1,\"name\":\"Rental 1\",\"surface\":100,\"price\":1000,\"picture\":\"https://example.com/picture1.jpg\",\"description\":\"Description 1\",\"ownerId\":1,\"created_at\":\"2022-01-01\",\"updated_at\":\"2022-01-01\"}")
      )),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content()})
  }, security = {@SecurityRequirement(name = "bearerAuth")})
  public ResponseEntity<RentalDTO> getRentalById(@PathVariable(required = true) Long rentalId) {
    RentalDTO rentalDTO = rentalService.getRentalDTOById(rentalId);

    return rentalDTO != null ?
        new ResponseEntity<>(rentalDTO, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

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
  public ResponseEntity<?> createRental(
      @ModelAttribute CreateRentalDTO createRentalDTO,
      @RequestHeader("Authorization") String token) {

    String username = jwtService.extractUsername(token.substring(7));
    UserEntity owner = userService.findUserByEmail(username);

    if (owner == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    Rental rental = new Rental();
    rental.setName(createRentalDTO.getName());
    rental.setSurface(createRentalDTO.getSurface());
    rental.setPrice(createRentalDTO.getPrice());
    rental.setDescription(createRentalDTO.getDescription());
    rental.setOwner(owner);
    rental.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    rental.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    MultipartFile picture = createRentalDTO.getPicture();

    if (picture != null && !picture.isEmpty()) {
      //TODO check que le nom n'est pas déjas pris
      String picturePath = "./pictures/" + picture.getOriginalFilename();
      try {
        Files.write(Paths.get(picturePath), picture.getBytes());
        rental.setPicture(picture.getOriginalFilename());
      } catch (IOException e) {
        e.printStackTrace();

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
    }
    rentalService.saveRental(rental);

    return new ResponseEntity<>(new MessageResponse("Rental created !"), HttpStatus.OK);
  }

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
  public ResponseEntity<?> updateRental(
      @PathVariable Long rentalId,
      @ModelAttribute CreateRentalDTO createRentalDTO,
      @RequestHeader("Authorization") String token) {

    String username = jwtService.extractUsername(token.substring(7));
    UserEntity owner = userService.findUserByEmail(username);
    Rental rental = rentalService.findRentalById(rentalId);

    if (owner == null ||
        rental == null ||
        !rental.getOwner().getId().equals(owner.getId())) {

      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    rental.setName(createRentalDTO.getName());
    rental.setSurface(createRentalDTO.getSurface());
    rental.setPrice(createRentalDTO.getPrice());
    rental.setDescription(createRentalDTO.getDescription());
    rental.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    rentalService.saveRental(rental);

    return new ResponseEntity<>(new MessageResponse("Rental updated !"), HttpStatus.OK);
  }
}