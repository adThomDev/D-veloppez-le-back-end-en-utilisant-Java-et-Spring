package com.ocr.p3back.controller;

import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.service.RentalService;
import com.ocr.p3back.service.UserService;
import com.ocr.p3back.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/{id}")
  public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
    RentalDTO rentalDTO = rentalService.getRentalDTOById(id);
    if (rentalDTO != null) {
      return ResponseEntity.ok(rentalDTO);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  //TODO : le post et le put mapping
}