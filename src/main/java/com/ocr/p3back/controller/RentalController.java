package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}