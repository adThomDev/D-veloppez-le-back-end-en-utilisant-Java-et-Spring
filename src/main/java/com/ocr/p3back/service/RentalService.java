package com.ocr.p3back.service;

import com.ocr.p3back.dao.RentalsRepository;
import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {
  private final RentalsRepository rentalsRepository;

  @Autowired
  public RentalService(RentalsRepository rentalsRepository) {
    this.rentalsRepository = rentalsRepository;
  }

  public List<RentalDTO> getAllRentals() {
    List<Rental> rentals = rentalsRepository.findAll();
    return rentals.stream()
        .map(rental -> {
          RentalDTO dto = new RentalDTO();
          dto.setId(rental.getId());
          dto.setName(rental.getName());
          dto.setSurface(rental.getSurface());
          dto.setPrice(rental.getPrice());
          dto.setPicture(rental.getPicture());
          dto.setDescription(rental.getDescription());
          dto.setOwnerId(rental.getOwner().getId());
          return dto;
        })
        .collect(Collectors.toList());
  }

  public RentalDTO getRentalById(Long id) {
    Rental rental = rentalsRepository.findById(id).orElseThrow(() -> new RuntimeException("Rental not found"));
    RentalDTO rentalDTO = new RentalDTO();
    rentalDTO.setId(rental.getId());
    rentalDTO.setName(rental.getName());
    rentalDTO.setSurface(rental.getSurface());
    rentalDTO.setPrice(rental.getPrice());
    rentalDTO.setPicture(rental.getPicture());
    rentalDTO.setDescription(rental.getDescription());
    rentalDTO.setOwnerId(rental.getOwner().getId());
    return rentalDTO;
  }
}