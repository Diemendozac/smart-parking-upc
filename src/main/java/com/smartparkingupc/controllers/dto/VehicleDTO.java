package com.smartparkingupc.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDTO {
  private String plate;
  private String brand;
  private int model;
  private String line;
  //private String color;
  private boolean isOwner;
}
