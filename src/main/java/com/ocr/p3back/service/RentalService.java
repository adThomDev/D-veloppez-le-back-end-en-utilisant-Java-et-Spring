package com.ocr.p3back.service;

import com.ocr.p3back.dao.RentalRepository;
import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RentalService {
  @Autowired
  private RentalRepository rentalRepository;
//  private final RentalRepository rentalRepository;
//
//  @Autowired
//  public RentalService(RentalRepository rentalRepository) {
//    this.rentalRepository = rentalRepository;
//  }

  //TODO utilité ?
//  @Value("${image.upload.dir:/images}")
//  private String imageUploadDir;

  public Map<String, List<RentalDTO>> getAllRentals() {
    List<Rental> rentals = rentalRepository.findAll();
    List<RentalDTO> rentalDTOs = rentals.stream()
        .map(rental -> {
          RentalDTO dto = new RentalDTO();
          dto.setId(rental.getId());
          dto.setName(rental.getName());
          dto.setSurface(rental.getSurface());
          dto.setPrice(rental.getPrice());
          dto.setDescription(rental.getDescription());
          dto.setOwnerId(rental.getOwner().getId());
          //TODO : utiliser une constante pour la base url
          String picturePath = "http://localhost:3001/pictures/" + rental.getPicture();
          dto.setPicture(picturePath);

          return dto;
        })
        .collect(Collectors.toList());

    Map<String, List<RentalDTO>> rentalsMap = new HashMap<>();
    rentalsMap.put("rentals", rentalDTOs);
    return rentalsMap;
  }

  public RentalDTO getRentalDTOById(Long id) {
    Rental rental = rentalRepository.findById(id).orElse(null);
    if (rental != null) {
      return convertToDTO(rental);
    }
    return null;
  }

  private RentalDTO convertToDTO(Rental rental) {
    RentalDTO dto = new RentalDTO();
    dto.setId(rental.getId());
    dto.setName(rental.getName());
    dto.setSurface(rental.getSurface());
    dto.setPrice(rental.getPrice());
    dto.setDescription(rental.getDescription());
    dto.setOwnerId(rental.getOwner().getId());

    // Update the picture field to include the full path
    String picturePath = "http://localhost:3001/pictures/" + rental.getPicture();
    dto.setPicture(picturePath);

    return dto;
  }

  public Rental createRental(Rental rental) {
//    rental.setCreatedAt(new Date());
//    rental.setUpdatedAt(new Date());
    return rentalRepository.save(rental);
  }

  public Rental findById(Long id) {
    return rentalRepository.findById(id).orElse(null);
  }

  public Rental updateRental(Rental rental) {
    return rentalRepository.save(rental);
  }

}