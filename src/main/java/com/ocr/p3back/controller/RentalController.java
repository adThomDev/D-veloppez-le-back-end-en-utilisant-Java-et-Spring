package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/rentals")
public class RentalController {
  private final RentalService rentalService;

  @Autowired
  public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @GetMapping
  public ResponseEntity<List<RentalDTO>> getAllRentals() {
    List<RentalDTO> rentals = rentalService.getAllRentals();
    return ResponseEntity.ok(rentals);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
    RentalDTO rentalDTO = rentalService.getRentalById(id);
    if (rentalDTO != null) {
      return ResponseEntity.ok(rentalDTO);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

//  @PostMapping("/{ownerId}")
//  public ResponseEntity<RentalDTO> createRental(
//      @PathVariable Long ownerId,
//      @RequestParam String name,
//      @RequestParam String surface,
//      @RequestParam String price,
//      @RequestParam String description,
//      @RequestPart(required = false) MultipartFile picture) {
//
//    RentalDTO rentalDTO = new RentalDTO();
//    rentalDTO.setName(name);
//    rentalDTO.setSurface(Long.parseLong(surface));
//    rentalDTO.setPrice(Long.parseLong(price));
//    rentalDTO.setDescription(description);
//    rentalDTO.setOwnerId(ownerId);
//
//    // Handle file upload if a picture is provided
//    if (picture != null && !picture.isEmpty()) {
//      // You can implement the logic to save the picture and set the file path in the DTO
//      // For simplicity, we'll just set a placeholder value here
//      rentalDTO.setPicture(picture.getOriginalFilename());
//    }
//
//    RentalDTO newRental = rentalService.createRental(rentalDTO);
//    return ResponseEntity.ok(newRental);
//  }
}