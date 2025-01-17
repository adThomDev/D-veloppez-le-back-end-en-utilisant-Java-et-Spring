package com.ocr.p3back.service;

import com.ocr.p3back.dao.UserRepository;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.model.dto.user.UserDTO;
import com.ocr.p3back.model.dto.user.UserRegistrationDTO;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository,
                     JwtService jwtService,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
  }

  public UserEntity findUserByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  public UserEntity findUserById(Long userId) {
    return userRepository.findById(userId).orElse(null);
  }

  /**
   * Gets the information for the currently logged-in user.
   *
   * @param request The data containing the user's JWT.
   * @return The user's information on success, or a 401.
   */
  public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
    final String authHeader = request.getHeader("Authorization");

    try {
      String token = authHeader.replace("Bearer ", "");
      String email = jwtService.getClaims(token).getSubject();
      UserEntity user = findUserByEmail(email);

      if (user != null) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        userDTO.setCreated_at(dateFormat.format(user.getCreatedAt()));
        userDTO.setUpdated_at(dateFormat.format(user.getUpdatedAt()));

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  /**
   * Creates a new user and returns his JWT if successful.
   *
   * @param userRegistrationDTO The user details for registration encapsulated in a UserDTO object.
   * @return A ResponseEntity containing an AuthResponseDTO with the generated JWT token if the user is successfully created,
   * or a ResponseEntity with BAD_REQUEST status if the user already exists or if the request data is incomplete.
   */
  public ResponseEntity<?> createUser(UserRegistrationDTO userRegistrationDTO) {
    if (findUserByEmail(userRegistrationDTO.getEmail()) != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    if (!userRegistrationDTO.getName().isEmpty() &&
        !userRegistrationDTO.getEmail().isEmpty() &&
        !userRegistrationDTO.getPassword().isEmpty()) {
      UserEntity newUser = new UserEntity();
      newUser.setName(userRegistrationDTO.getName());
      newUser.setEmail(userRegistrationDTO.getEmail());
      newUser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
      newUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      userRepository.save(newUser);
      AuthResponseDTO authResponseDTO = new AuthResponseDTO(jwtService.generateToken(newUser.getEmail()));

      return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
  
  /**
   * Gets the information for a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The user's information as a UserDTO, or null if not found or an error occurs.
   */
  public UserDTO getUserById(Long id) {
    try {
      UserEntity user = findUserById(id);

      if (user != null) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        userDTO.setCreated_at(dateFormat.format(user.getCreatedAt()));
        userDTO.setUpdated_at(dateFormat.format(user.getUpdatedAt()));

        return userDTO;
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

}
