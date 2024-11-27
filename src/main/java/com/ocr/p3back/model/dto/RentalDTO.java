package com.ocr.p3back.model.dto;

public class RentalDTO {
  private Long id;
  private String name;
  private Long surface;
  private Long price;
  private String picture;
  private String description;
  private Long ownerId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSurface() {
    return surface;
  }

  public void setSurface(Long surface) {
    this.surface = surface;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }
}

