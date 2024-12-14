package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.RentalService;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<RentalDTO> rentals = rentalService.getAllRentals();
    rentals.forEach(rental -> rental.setPicture("http://localhost:3001/pictures/" + rental.getPicture()));
    Map<String, List<RentalDTO>> rentalsMap = new HashMap<String, List<RentalDTO>>();
    rentalsMap.put("rentals", rentals);
    return ResponseEntity.ok(rentalsMap);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
    RentalDTO rentalDTO = rentalService.getRentalDTOById(id);
    if (rentalDTO != null) {
      return ResponseEntity.ok(rentalDTO);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  //TODO s'assurer que c'est bien l'owner
  public ResponseEntity<Rental> createRental(
      @RequestParam String name,
      @RequestParam Long surface,
      @RequestParam Long price,
      @RequestParam String description,
      @RequestPart(required = false) MultipartFile picture,
      @RequestHeader("Authorization") String token) {

    // Extract username from token
    String username = jwtService.extractUsername(token.substring(7)); // Assuming 'Bearer ' prefix
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

    // Handle file upload if a picture is provided
    if (picture != null && !picture.isEmpty()) {
      try {
        String fileName = picture.getOriginalFilename();
        Path filePath = Paths.get("/pictures", fileName);
        Files.copy(picture.getInputStream(), filePath);

        // Set the file path in the entity
        rental.setPicture(filePath.toString());
      } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    }

    rental.setCreatedAt(new Date());
    rental.setUpdatedAt(new Date());

    Rental newRental = rentalService.createRental(rental);
    return ResponseEntity.ok(newRental);
  }

  @PutMapping("/{rentalId}")
  public ResponseEntity<Rental> updateRental(
      @PathVariable Long rentalId,
      @RequestParam String name,
      @RequestParam String surface,
      @RequestParam String price,
      @RequestParam String description,
      @RequestPart(required = false) MultipartFile picture) {

    Rental rental = rentalService.findById(rentalId);
    if (rental == null) {
      return ResponseEntity.notFound().build();
    }

    rental.setName(name);
    rental.setSurface(Long.parseLong(surface));
    rental.setPrice(Long.parseLong(price));
    rental.setDescription(description);

    if (picture != null && !picture.isEmpty()) {
      rental.setPicture(picture.getOriginalFilename());
    }

    rental.setUpdatedAt(new Date());

    Rental updatedRental = rentalService.updateRental(rental);
    return ResponseEntity.ok(updatedRental);
  }
}
