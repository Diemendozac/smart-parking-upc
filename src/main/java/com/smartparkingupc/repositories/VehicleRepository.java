package com.smartparkingupc.repositories;

import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

  List<Vehicle> findAllByOwnerId(Long ownerId);
  Optional<Vehicle> findByPlate(String plate);
  Optional<Long> findOwnerIdByPlate(String plate);
  @Query("SELECT v FROM Vehicle v where v.isParked = true")
  List<VehicleDTO> findAllParkedVehicles();
  void deleteByPlate(String plate);

}
