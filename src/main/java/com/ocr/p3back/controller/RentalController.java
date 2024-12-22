package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.RentalService;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  @GetMapping
  public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
    Map<String, List<RentalDTO>> rentalsMap = rentalService.getAllRentals();
    return ResponseEntity.ok(rentalsMap);
  }

  @GetMapping("/{rentalId}")
  public ResponseEntity<RentalDTO> getRentalById(@PathVariable(required = true) Long rentalId) {
    RentalDTO rentalDTO = rentalService.getRentalDTOById(rentalId);
    if (rentalDTO != null) {
      return ResponseEntity.ok(rentalDTO);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<?> createRental(
      @RequestParam(required = true) String name,
      @RequestParam(required = true) Long surface,
      @RequestParam(required = true) Long price,
      @RequestParam(required = true) String description,
      @RequestPart(required = true) MultipartFile picture,
      @RequestHeader("Authorization") String token) {

    String username = jwtService.extractUsername(token.substring(7));
    UserEntity owner = userService.findUserByEmail(username);

    if (owner == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Rental rental = new Rental();
    rental.setName(name);
    rental.setSurface(surface);
    rental.setPrice(price);
    rental.setDescription(description);
    rental.setOwner(owner);
    rental.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    rental.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

    if (picture != null && !picture.isEmpty()) {
      String picturePath = "./pictures/" + picture.getOriginalFilename();
      try {
        Files.write(Paths.get(picturePath), picture.getBytes());
        rental.setPicture(picture.getOriginalFilename());
      } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    }

    rentalService.createRental(rental);

    return ResponseEntity.ok().body(Collections.singletonMap("message", "Rental created !"));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateRental(
      @PathVariable Long id,
      @RequestParam String name,
      @RequestParam Long surface,
      @RequestParam Long price,
      @RequestParam String description,
      @RequestHeader("Authorization") String token) {

    String username = jwtService.extractUsername(token.substring(7));
    UserEntity owner = userService.findUserByEmail(username);

    if (owner == null || rentalService.findRentalById(id) == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Rental rental = rentalService.findRentalById(id);

    if (!rental.getOwner().getId().equals(owner.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    rental.setName(name);
    rental.setSurface(surface);
    rental.setPrice(price);
    rental.setDescription(description);
    rental.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

    rentalService.updateRental(rental);

    return ResponseEntity.ok().body(Collections.singletonMap("message", "Rental updated !"));
  }
}