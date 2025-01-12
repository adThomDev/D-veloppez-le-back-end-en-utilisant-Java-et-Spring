package com.ocr.p3back.service.auth;

import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  @Autowired
  public UserDetailsServiceImpl(UserService userService) {
    this.userService = userService;
  }

  /**
   * Loads user details by their username.
   *
   * @param username the email (username) of the user to be loaded.
   * @return A UserDetails object containing the user's username, password, and authorities.
   * @throws UsernameNotFoundException if the user is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userService.findUserByEmail(username);

    if (user != null) {
      return new User(username, user.getPassword(), Collections.emptyList());
    } else {
      Exception e = new Exception("User not found");

      throw new UsernameNotFoundException(e.getMessage());
    }
  }
}