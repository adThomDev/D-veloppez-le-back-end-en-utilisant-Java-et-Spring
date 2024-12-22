package com.ocr.p3back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "email", length = 255, unique = true, nullable = false)
  private String email;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "password", length = 255, nullable = false)
  private String password;

  @OneToMany(mappedBy = "owner")
  private List<Rental> rentals;

  @OneToMany(mappedBy = "userEntity")
  private List<Message> messages;

  @Column(updatable = false, name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Timestamp createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT '0000-00-00 00:00:00'")
  private Timestamp updatedAt;
}