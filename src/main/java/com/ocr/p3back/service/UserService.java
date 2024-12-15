package com.ocr.p3back.service;

import com.ocr.p3back.exception.auth.DuplicationException;
import com.ocr.p3back.exception.auth.NotFoundException;
import com.ocr.p3back.model.ErrorResponse;
import com.ocr.p3back.model.dto.UserDTO;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.dao.UserRepository;
import com.ocr.p3back.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private JwtService jwtService;

  public UserEntity findUserByEmail(String email) {
    return repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found: " + email));
  }

  public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
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
        userDTO.setCreatedAt(new Date(user.getCreatedAt().getTime()));
        userDTO.setUpdatedAt(new Date(user.getUpdatedAt().getTime()));

        return ResponseEntity.ok(userDTO);
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("User not found"));
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Invalid token"));
    }
  }

//  TODO : enlever ?
  /**
   * Vérifie que l'adresses e-mail pour un utilisateur n'existe pas déjà dans la BDD.
   *
   * @param utilisateur L'utilisateur pour lequel vérifier la duplication d'e-mail.
   * @throws DuplicationException si une duplication d'e-mail est détectée.
   */
  private void checkEmailDuplication(UserEntity utilisateur) {
    final String email = utilisateur.getEmail();

    if (email != null && email.length() > 0) {
      final Long id = utilisateur.getId();
      final UserEntity util2 = repository.findByEmail(email).orElse(null);

      if (util2 != null && Objects.equals(util2.getEmail(), email) && !Objects.equals(util2.getId(), id)) {
        throw new DuplicationException("Email duplication: " + email);
      }
    }
  }

}
