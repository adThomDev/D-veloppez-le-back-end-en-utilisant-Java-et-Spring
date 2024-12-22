package com.ocr.p3back.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
  private Long id;
  private String name;
  private String email;
  private Date createdAt;
  private Date updatedAt;
}

