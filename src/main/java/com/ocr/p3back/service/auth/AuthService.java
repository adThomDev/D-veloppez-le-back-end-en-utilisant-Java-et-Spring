package com.ocr.p3back.service.auth;

import com.ocr.p3back.model.dto.auth.AuthRequestDTO;
import com.ocr.p3back.model.dto.auth.AuthResponseDTO;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private UserService userService; // Service pour gérer les informations des utilisateurs

  @Autowired
  private PasswordEncoder passwordEncoder; // Utilitaire pour encoder les mots de passe

  @Autowired
  private JwtService jwtService; // Service pour la gestion des JWT (JSON Web Tokens)

  @Autowired
  private AuthenticationManager authenticationManager; // Gestionnaire d'authentification

  // Méthode d'authentification d'un utilisateur existant
  public AuthResponseDTO authenticate(AuthRequestDTO authRequestDTOdto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequestDTOdto.getEmail(), authRequestDTOdto.getPassword()));
    // Utilise le gestionnaire d'authentification pour valider les informations d'identification (log/mp) (voir UserDetailsService/loadUserByUsername)

    final UserEntity userEntity = userService.findUserByEmail(authRequestDTOdto.getEmail());
    // Récupère l'utilisateur correspondant à l'e-mail fourni (si on av accé à authenticationManager.authenticate on aurait pu à la place renvoyer direct un UserEntity)

    return new AuthResponseDTO(jwtService.generateToken(userEntity.getEmail()));
    // Génère un JWT pour l'utilisateur authentifié et le renvoie dans une réponse
  }

  //  public AuthResponseDTO register(RegisterRequestDTO dto) {
  //    // Méthode d'inscription d'un nouvel utilisateur
  //
  //    UserEntity userEntity = new UserEntity();
  //    userEntity.setNom(dto.getName());
  //    userEntity.setEmail(dto.getEmail());
  //    userEntity.setMdp(passwordEncoder.encode(dto.getPassword()));
  //    // Crée un nouvel utilisateur en utilisant les informations fournies
  //
  //    userEntity = userService.create(userEntity);
  //    // Appelle le service utilisateur pour enregistrer l'utilisateur dans la base de données
  //
  //    return new AuthResponseDTO(jwtService.generateToken(userEntity.getEmail()));
  //    // Génère un JWT pour l'utilisateur nouvellement inscrit et le renvoie dans une réponse
  //  }
}

