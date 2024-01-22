package com.smartparkingupc.controllers;


import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.http.response.EntityResponse;
import com.smartparkingupc.services.IVehicleService;
import com.smartparkingupc.utils.ResponseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

  @Autowired
  private IVehicleService vehicleService;

  @GetMapping("/all")
  public ResponseEntity<?> getVehicles() {
    return ResponseEntity.ok(vehicleService.findAll());
  }

  @GetMapping("/")
  public ResponseEntity<Vehicle> findVehicleByPlate(@RequestParam String plate) {

    Optional<Vehicle> optionalVehicle = vehicleService.findVehicleByPlate(plate);
    if (optionalVehicle.isPresent()) {
      Vehicle vehicle = optionalVehicle.get();
      return ResponseEntity.ok(vehicle);
    }
    return ResponseEntity.noContent().build();
  }


  @PostMapping("/save")
  public ResponseEntity<?> saveVehicle(@RequestAttribute("LoggedInUser") String email, @RequestBody VehicleDTO vehicleDTO) {

    Long requestId = vehicleService.findOwnerRequestIdByUserEmail(email);
    if (requestId == -1) return EntityResponse.generateResponse(
            ResponseConstants.ISSUE_WHILE_SAVING_VEHICLE, HttpStatus.NOT_FOUND, "Not found"
    );

    Vehicle vehicle = Vehicle.builder()
            .plate(vehicleDTO.getPlate().toUpperCase())
            .model(vehicleDTO.getModel())
            .line(vehicleDTO.getLine())
            .brand(vehicleDTO.getBrand())
            .ownerId(requestId)
            .isParked(false)
            .build();
    vehicleService.save(vehicle, requestId);
    return EntityResponse.generateResponse(
            ResponseConstants.SAVED_VEHICLE_MESSAGE, HttpStatus.OK, "Saved Vehicle");
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateVehicle(@RequestAttribute("LoggedInUser") String email, @RequestBody VehicleDTO vehicleDTO) {

    Optional<Vehicle> optVehicle = vehicleService.findVehicleByPlate(vehicleDTO.getPlate().toUpperCase());
    Long requestId = vehicleService.findOwnerRequestIdByUserEmail(email);
    if (optVehicle.isEmpty()) return ResponseEntity.noContent().build();
    if (requestId == -1) return ResponseEntity.internalServerError().build();
    Vehicle vehicle = optVehicle.get();
    if (!requestId.equals(vehicle.getOwnerId())) return new ResponseEntity<>("No eres el dueño del vehículo", HttpStatus.UNAUTHORIZED);
    Vehicle updatedVehicle = Vehicle.builder()
            .id(vehicle.getId())
            .plate(vehicle.getPlate())
            .brand(vehicleDTO.getBrand())
            .model(vehicleDTO.getModel())
            .line(vehicleDTO.getLine())
            .ownerId(requestId)
            .isParked(vehicle.isParked())
            .build();
    vehicleService.updateVehicle(updatedVehicle);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteVehicle(@RequestAttribute("LoggedInUser") String email, @RequestParam String vehiclePlate) {

    Optional<Vehicle> optVehicle = vehicleService.findVehicleByPlate(vehiclePlate);
    Long requestId = vehicleService.findOwnerRequestIdByUserEmail(email);
    if (optVehicle.isEmpty()) return ResponseEntity.badRequest().build();
    if (requestId == -1) return ResponseEntity.internalServerError().build();
    if (requestId.equals(optVehicle.get().getOwnerId())) {
      vehicleService.deleteByPlate(vehiclePlate);
      return ResponseEntity.ok().build();
    }
    return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

  }

}
