package com.ocr.p3back.controller;


import com.ocr.p3back.model.dto.UserDTO;
import com.ocr.p3back.model.entity.UserEntity;
import com.ocr.p3back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
    UserEntity userEntity = userService.findUserById(id);
    if (userEntity == null) {
      return ResponseEntity.notFound().build();
    }

    UserDTO userDTO = new UserDTO();
    userDTO.setId(userEntity.getId());
    userDTO.setName(userEntity.getName());
    userDTO.setEmail(userEntity.getEmail());
    userDTO.setCreatedAt(userEntity.getCreatedAt());
    userDTO.setUpdatedAt(userEntity.getUpdatedAt());

    return ResponseEntity.ok(userDTO);
  }
}
