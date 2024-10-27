package com.ocr.p3back.service;

import com.ocr.p3back.dao.RentalsRepository;
import com.ocr.p3back.model.entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {
  private final RentalsRepository rentalsRepository;

  @Autowired
  public RentalService(RentalsRepository rentalsRepository) {
    this.rentalsRepository = rentalsRepository;
  }

  public List<Rental> getAllRentals() {
    return rentalsRepository.findAll();
  }

  public Rental getRentalById(Long id) {
    return rentalsRepository.findById(id).orElse(null);
  }
}