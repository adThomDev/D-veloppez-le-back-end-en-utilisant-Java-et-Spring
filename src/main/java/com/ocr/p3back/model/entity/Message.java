package com.ocr.p3back.model.entity;

import jakarta.persistence.*;

@Entity
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
}