package com.ocr.p3back.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {
  private String name;
  private String email;
  private String password;
}
