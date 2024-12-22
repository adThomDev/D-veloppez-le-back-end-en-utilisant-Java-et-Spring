package com.ocr.p3back.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalDTO {
  private Long id;
  private String name;
  private Long surface;
  private Long price;
  private String picture;
  private String description;
  private Long ownerId;
  private String created_at;
  private String updated_at;
}

