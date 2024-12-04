package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

  List<Vehicle> findAllByOwnerId(Long ownerId);

  Optional<Vehicle> findByPlate(String plate);

  @Query(value = "SELECT * FROM vehicle WHERE is_parked = true", nativeQuery = true)
  List<Vehicle> findAllParkedVehicles();

  void deleteVehicleByPlate(String plate);
}
