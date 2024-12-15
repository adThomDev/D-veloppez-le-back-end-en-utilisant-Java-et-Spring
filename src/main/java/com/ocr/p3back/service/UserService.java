package com.ocr.p3back.service;

import com.ocr.p3back.exception.auth.DuplicationException;
import com.ocr.p3back.exception.auth.NotFoundException;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  public UserEntity findUserByEmail(String email) {
    return repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found: " + email));
  }

  public UserEntity findUserById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
  }

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
