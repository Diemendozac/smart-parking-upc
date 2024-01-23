package com.smartparkingupc.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String plate;
  private String brand;
  private int model;
  private String line;
  //private CEnum color;
  @Column(name = "owner_id")
  private Long ownerId;
  @Column(name = "is_parked")
  private boolean isParked;

}
