package com.ocr.p3back.model.dto.auth;

//TODO check l'utilité
public class AuthResponseDTO {

  private String token;

  public AuthResponseDTO(String token) {
    super();
    this.token = token;
  }

  public String getToken() {
    return token;
  }

}