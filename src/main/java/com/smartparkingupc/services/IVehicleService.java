package com.smartparkingupc.services;

import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.Vehicle;

import java.util.List;
import java.util.Optional;

public interface IVehicleService {

  List<Vehicle> findAll();
  Optional<Vehicle> findVehicleByPlate(String plate);
  void save(Vehicle vehicle, Long ownerId);
  void updateVehicle(Vehicle vehicle);
  void deleteByPlate(String plate);
  Long findOwnerRequestIdByUserEmail(String email);
  List<VehicleDTO> findAllParkedVehicles();

}
