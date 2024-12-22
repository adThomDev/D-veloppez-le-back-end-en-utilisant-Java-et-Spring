package com.ocr.p3back.service;

import com.ocr.p3back.dao.RentalRepository;
import com.ocr.p3back.model.dto.RentalDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RentalService {
  @Autowired
  private RentalRepository rentalRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private JwtService jwtService;

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


  public Rental createRental(Rental rental) {
    return rentalRepository.save(rental);
  }

//  public Rental createRental(
//      String name,
//      Long surface,
//      Long price,
//      String description,
//      MultipartFile picture,
//      String token) {
//
//    String username = jwtService.extractUsername(token.substring(7));
//    UserEntity owner = userService.findUserByEmail(username);
//
//    if (owner == null) {
//      throw new UnauthorizedException("Unauthorized");
//    }
//
//    Rental rental = new Rental();
//    rental.setName(name);
//    rental.setSurface(surface);
//    rental.setPrice(price);
//    rental.setDescription(description);
//    rental.setOwner(owner);
//    rental.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
//    rental.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
//
//    if (picture != null && !picture.isEmpty()) {
//      String picturePath = "./pictures/" + picture.getOriginalFilename();
//      try {
//        Files.write(Paths.get(picturePath), picture.getBytes());
//        rental.setPicture(picture.getOriginalFilename());
//      } catch (IOException e) {
//        throw new InternalServerErrorException("Error saving picture");
//      }
//    }
//
//    return rentalRepository.save(rental);
//  }

  public Rental findRentalById(Long id) {
    return rentalRepository.findById(id).orElse(null);
  }

  public Rental updateRental(Rental rental) {
    return rentalRepository.save(rental);
  }

}