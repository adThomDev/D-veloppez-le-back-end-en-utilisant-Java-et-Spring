package com.ocr.p3back.service.auth;

import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserService userService; // Service pour gérer les détails des utilisateurs

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Méthode pour charger les détails de l'utilisateur en fonction de son nom d'utilisateur (e-mail)

    try {
      UserEntity user = userService.findUserByEmail(username);
      // Récupère les informations de l'utilisateur à partir du service utilisateur

      String nomRole = user.getClass().getSimpleName();
      // Récupère le nom du rôle de l'utilisateur

      List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
      authorities.add(new SimpleGrantedAuthority(nomRole));
      // Crée une liste d'autorités (rôles) pour l'utilisateur basée sur son rôle (voir config/SecurityConfig)

      return new User(username, user.getPassword(), authorities);
      // Crée un objet UserDetails qui représente l'utilisateur avec son nom d'utilisateur, son mot de passe
      // et ses autorités (rôles)
    } catch (Exception e) {
      throw new UsernameNotFoundException(e.getMessage());
      // En cas d'erreur, lance une exception UsernameNotFoundException
    }
  }
}

