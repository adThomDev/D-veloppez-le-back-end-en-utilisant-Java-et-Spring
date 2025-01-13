package com.ocr.p3back.model.dto.auth;

import lombok.Getter;

@Getter
public class AuthResponseDTO {
  private String token;

  public AuthResponseDTO(String token) {
    super();
    this.token = token;
  }
}