package com.ocr.p3back.service;

import com.ocr.p3back.dao.RentalRepository;
import com.ocr.p3back.model.dto.message.MessageResponse;
import com.ocr.p3back.model.dto.rental.CreateRentalDTO;
import com.ocr.p3back.model.dto.rental.RentalDTO;
import com.ocr.p3back.model.dto.rental.RentalsDTO;
import com.ocr.p3back.model.entity.Rental;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

  private final String picturesUrl = "http://localhost:3001/pictures/";

  /**
   * Retrieves a list of all available rentals.
   *
   * @return ResponseEntity<RentalsDTO> containing a list of {@link RentalDTO} objects representing all rentals.
   */
  public ResponseEntity<RentalsDTO> getAllRentals() {
    List<Rental> rentals = rentalRepository.findAll();
    List<RentalDTO> rentalDTOList = rentals.stream()
        .map(rental -> {
          RentalDTO rentalDTO = new RentalDTO();
          rentalDTO.setId(rental.getId());
          rentalDTO.setName(rental.getName());
          rentalDTO.setSurface(rental.getSurface());
          rentalDTO.setPrice(rental.getPrice());
          rentalDTO.setDescription(rental.getDescription());
          rentalDTO.setOwnerId(rental.getOwner().getId());
          String picturePath = picturesUrl + rental.getPicture();
          rentalDTO.setPicture(picturePath);

          return rentalDTO;
        })
        .collect(Collectors.toList());
    RentalsDTO rentalsDTO = new RentalsDTO(rentalDTOList);

    return ResponseEntity.status(HttpStatus.OK).body(rentalsDTO);
  }

  /**
   * Retrieves a specific rental by its ID.
   *
   * @param rentalId The ID of the rental to retrieve.
   * @return ResponseEntity<RentalDTO> containing the {@link RentalDTO} if found,
   * or a 401 Unauthorized status if not found.
   */
  public ResponseEntity<RentalDTO> getRentalDTOById(Long rentalId) {
    Rental rental = rentalRepository.findById(rentalId).orElse(null);

    if (rental != null) {
      return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(rental));
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  /**
   * Transforms a {@link Rental} object into a {@link RentalDTO} object.
   *
   * @param rental The {@link Rental} object to convert.
   * @return A {@link RentalDTO} object containing the converted data.
   */
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
    String picturePath = picturesUrl + rental.getPicture();
    rentalDTO.setPicture(picturePath);

    return rentalDTO;
  }

  /**
   * Creates a new rental.
   *
   * @param createRentalDTO A {@link CreateRentalDTO} object containing the rental details to be created.
   * @param authToken       The authentication token of the user creating the rental.
   * @return ResponseEntity containing a {@link MessageResponse} with a success message upon successful creation,
   * or a 401 Unauthorized status if user is not authorized or an error occurs.
   */
  public ResponseEntity<MessageResponse> createRental(CreateRentalDTO createRentalDTO, String authToken) {
    String username = jwtService.extractUsername(authToken.substring(7));
    UserEntity owner = userService.findUserByEmail(username);

    if (owner == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Rental rental = new Rental();
    rental.setName(createRentalDTO.getName());
    rental.setSurface(createRentalDTO.getSurface());
    rental.setPrice(createRentalDTO.getPrice());
    rental.setDescription(createRentalDTO.getDescription());
    rental.setOwner(owner);
    rental.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    rental.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    MultipartFile picture = createRentalDTO.getPicture();

    if (picture != null && !picture.isEmpty()) {
      //TODO check que le nom n'est pas déjas pris
      String picturePath = "./pictures/" + picture.getOriginalFilename();
      try {
        Files.write(Paths.get(picturePath), picture.getBytes());
        rental.setPicture(picture.getOriginalFilename());
      } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
    }
    saveRental(rental);

    return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Rental created !"));
  }

  /**
   * Updates an existing rental.
   *
   * @param rentalId        The ID of the rental to update.
   * @param createRentalDTO A {@link CreateRentalDTO} object containing the updated rental details.
   * @param token           The authentication token of the user updating the rental.
   * @return ResponseEntity containing a {@link MessageResponse} with a success message upon successful update,
   * or a 401 Unauthorized status if user is not authorized or there's nothing to update.
   */
  public ResponseEntity<MessageResponse> updateRental(Long rentalId, CreateRentalDTO createRentalDTO, String token) {
    String username = jwtService.extractUsername(token.substring(7));
    UserEntity owner = userService.findUserByEmail(username);
    Rental rental = findRentalById(rentalId);

    if (owner == null ||
        rental == null ||
        !rental.getOwner().getId().equals(owner.getId())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String newName = createRentalDTO.getName();
    Long newSurface = createRentalDTO.getSurface();
    Long newPrice = createRentalDTO.getPrice();
    String newDescription = createRentalDTO.getDescription();
    Boolean toUpdate = false;

    if (newName != null && !newName.isEmpty()) {
      rental.setName(newName);
      toUpdate = true;
    }
    if (newSurface != null && newSurface > 0) {
      rental.setSurface(newSurface);
      toUpdate = true;
    }
    if (newPrice != null && newPrice >= 0) {
      rental.setPrice(newPrice);
      toUpdate = true;
    }
    if (newDescription != null && !newDescription.isEmpty()) {
      rental.setDescription(newDescription);
      toUpdate = true;
    }

    if (toUpdate) {
      rental.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      saveRental(rental);

      return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Rental updated !"));
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  public void saveRental(Rental rental) {
    rentalRepository.save(rental);
  }

  public Rental findRentalById(Long id) {
    return rentalRepository.findById(id).orElse(null);
  }
}