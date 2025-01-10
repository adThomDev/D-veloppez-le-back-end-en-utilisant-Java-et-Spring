package com.ocr.p3back.service;

import com.ocr.p3back.dao.UserRepository;
import com.ocr.p3back.exception.auth.DuplicationException;
import com.ocr.p3back.model.dto.UserDTO;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Objects;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private JwtService jwtService;

  public UserEntity findUserByEmail(String email) {

    return repository.findByEmail(email).orElse(null);
  }

  public UserEntity findUserById(Long userId) {

    return repository.findById(userId).orElse(null);
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

//  TODO : implémenter

  /**
   * Checks if the email address for a user does not already exist in the database.
   *
   * @param utilisateur The user for whom to check for email duplication.
   * @throws DuplicationException if an email duplication is detected.
   */
  private void checkEmailDuplication(UserEntity utilisateur) {
    final String email = utilisateur.getEmail();

    if (email != null && !email.isEmpty()) {
      final Long id = utilisateur.getId();
      final UserEntity util2 = repository.findByEmail(email).orElse(null);

      if (util2 != null && Objects.equals(util2.getEmail(), email) && !Objects.equals(util2.getId(), id)) {
        throw new DuplicationException("Email duplication: " + email);
      }
    }
  }

}
