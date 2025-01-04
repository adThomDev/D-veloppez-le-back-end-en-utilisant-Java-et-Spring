package com.ocr.p3back.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RentalsDTO {
  private List<RentalDTO> rentals;

  public RentalsDTO(List<RentalDTO> rentals) {
    this.rentals = rentals;
  }
}
