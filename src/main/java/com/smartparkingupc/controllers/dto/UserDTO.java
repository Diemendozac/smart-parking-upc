package com.smartparkingupc.controllers.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

  private String email;
  private String name;
  private String phoneNumber;
  private List<VehicleDTO> associatedVehicles;
  private List<ConfidenceCircleDTO> confidenceCircle;

}
