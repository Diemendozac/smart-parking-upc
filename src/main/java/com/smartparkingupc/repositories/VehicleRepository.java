package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

  List<Vehicle> findAllByOwnerId(Long ownerId);
  Optional<Vehicle> findByPlate(String plate);
  Optional<Long> findOwnerIdByPlate(String plate);
  void deleteByPlate(String plate);

}
