package com.ocr.p3back.service;

import com.ocr.p3back.dao.RentalRepository;
import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {
  @Autowired
  private RentalRepository rentalRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private JwtService jwtService;

  private final String baseUrl = "http://localhost:3001/pictures/";

  public List<RentalDTO> getAllRentals() {
    List<Rental> rentals = rentalRepository.findAll();
    List<RentalDTO> rentalDTOs = rentals.stream()
        .map(rental -> {
          RentalDTO rentalDTO = new RentalDTO();
          rentalDTO.setId(rental.getId());
          rentalDTO.setName(rental.getName());
          rentalDTO.setSurface(rental.getSurface());
          rentalDTO.setPrice(rental.getPrice());
          rentalDTO.setDescription(rental.getDescription());
          rentalDTO.setOwnerId(rental.getOwner().getId());
          String picturePath = baseUrl + rental.getPicture();
          rentalDTO.setPicture(picturePath);

          return rentalDTO;
        })
        .collect(Collectors.toList());

    return rentalDTOs;
  }

  public RentalDTO getRentalDTOById(Long id) {
    Rental rental = rentalRepository.findById(id).orElse(null);
    if (rental != null) {

      return convertToDTO(rental);
    }

    return null;
  }

  private RentalDTO convertToDTO(Rental rental) {
    RentalDTO rentalDTO = new RentalDTO();
    rentalDTO.setId(rental.getId());
    rentalDTO.setName(rental.getName());
    rentalDTO.setSurface(rental.getSurface());
    rentalDTO.setPrice(rental.getPrice());
    rentalDTO.setDescription(rental.getDescription());
    rentalDTO.setOwnerId(rental.getOwner().getId());

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    rentalDTO.setCreated_at(dateFormat.format(rental.getCreatedAt()));
    rentalDTO.setUpdated_at(dateFormat.format(rental.getUpdatedAt()));

    String picturePath = "http://localhost:3001/pictures/" + rental.getPicture();
    rentalDTO.setPicture(picturePath);

    return rentalDTO;
  }

  public Rental saveRental(Rental rental) {
    return rentalRepository.save(rental);
  }

  public Rental findRentalById(Long id) {
    return rentalRepository.findById(id).orElse(null);
  }
}