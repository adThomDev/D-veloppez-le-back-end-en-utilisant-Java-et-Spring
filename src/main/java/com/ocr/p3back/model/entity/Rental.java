package com.ocr.p3back.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "rental")
@Getter
@Setter
public class Rental {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", length = 255)
  private String name;

  @Column(name = "surface")
  private Long surface;

  @Column(name = "price")
  private Long price;

  @Column(name = "picture", length = 255)
  private String picture;

  @Column(name = "description", length = 2000)
  private String description;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private UserEntity owner;

  @OneToMany(mappedBy = "rental")
  private List<Message> messages;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Timestamp createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT '0000-00-00 00:00:00'")
  private Timestamp updatedAt;
}