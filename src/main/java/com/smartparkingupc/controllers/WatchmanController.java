package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.http.response.UserEntityByWatchmanResponse;
import com.smartparkingupc.http.response.EntityResponse;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.services.IVehicleService;
import com.smartparkingupc.utils.ResponseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/watchman")
public class WatchmanController {

  @Autowired
  private IUserService userService;
  @Autowired
  private IVehicleService vehicleService;


  @GetMapping("/related-users")
  public ResponseEntity<?> findAllVehicleRelatedUsersByPlate (@RequestParam String plate) {

    Optional<Vehicle> optVehicle = vehicleService.findVehicleByPlate(plate);
    if (optVehicle.isEmpty()) return ResponseEntity.noContent().build();
    Long ownerId = optVehicle.get().getOwnerId();
    List<UserEntityByWatchmanResponse> vehicleRelatedUsers = userService.getVehicleRelatedUsers(ownerId);
    return EntityResponse.generateResponse(ResponseConstants.VEHICLE_RELATED_USERS_FOUND, HttpStatus.OK, vehicleRelatedUsers);

  }

  @GetMapping("/parked-vehicles")
  public ResponseEntity<?> findAllParkedVehicles() {
    return EntityResponse.generateResponse("Vehículos parqueados:",
            HttpStatus.OK, vehicleService.findAllParkedVehicles());

  }

}
