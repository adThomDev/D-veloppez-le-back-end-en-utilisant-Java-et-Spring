package com.ocr.p3back.controller;

import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public List<Rental> getAllRentals() {
    return rentalService.getAllRentals();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
    Rental rental = rentalService.getRentalById(id);
    if (rental != null) {
      return ResponseEntity.ok(rental);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}