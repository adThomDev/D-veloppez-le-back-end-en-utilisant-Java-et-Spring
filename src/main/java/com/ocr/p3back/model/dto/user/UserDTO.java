package com.ocr.p3back.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
  private Long id;
  private String name;
  private String email;
  private String password;
  private String created_at;
  private String updated_at;
}

