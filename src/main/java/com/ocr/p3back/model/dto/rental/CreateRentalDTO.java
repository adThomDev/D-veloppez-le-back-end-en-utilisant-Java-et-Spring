package com.ocr.p3back.model.dto.rental;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateRentalDTO {
  private String name;
  private Long surface;
  private Long price;
  private String description;
  private MultipartFile picture;
}
