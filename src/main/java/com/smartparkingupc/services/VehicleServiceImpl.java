package com.smartparkingupc.services;

import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class VehicleServiceImpl implements IVehicleService {

  @Autowired
  private VehicleRepository vehicleRepository;
  @Autowired
  private UserRepository userRepository;

  @Override
  public List<Vehicle> findAll() {
    return (List<Vehicle>) vehicleRepository.findAll();
  }

  @Override
  public Optional<Vehicle> findVehicleByPlate(String plate) {
    return vehicleRepository.findByPlate(plate);
  }


  @Override
  public void save(Vehicle vehicle, Long ownerId) {

    if( vehicleRepository.findAllByOwnerId(ownerId).size() > 2) return;
    vehicleRepository.save(vehicle);
  }

  @Override
  public void updateVehicle(Vehicle vehicle) {
    vehicleRepository.save(vehicle);
  }

  @Override
  @Transactional
  public void deleteByPlate(String plate) {
    vehicleRepository.deleteVehicleByPlate(plate);
  }

  @Override
  public Long findOwnerRequestIdByUserEmail(String email) {
    Optional<UserEntity> optUser = userRepository.findByEmail(email);
    if (optUser.isPresent()) return optUser.get().getId();
    return -1L;
  }

  @Override
  public List<VehicleDTO> findAllParkedVehicles() {
    return vehicleRepository.findAllParkedVehicles()
            .stream().map(vehicle -> VehicleDTO.builder()
                    .plate(vehicle.getPlate())
                    .brand(vehicle.getBrand())
                    .model(vehicle.getModel())
                    .line(vehicle.getLine())
                    .build()
            ).toList();
  }

  @Override
  public List<VehicleDTO> findAssociatedVehicles(Long requestId) {

    ArrayList<Long> associatedIds = new ArrayList<>();
    Optional<UserEntity> optUser = userRepository.findById(requestId);
    if ( optUser.isEmpty() ) return null;
    UserEntity user = optUser.get();
    associatedIds.add(requestId);
    user.getConfidenceCircle().forEach((confidenceCircleUser -> associatedIds.add(confidenceCircleUser.getId())));

    List<Vehicle> relatedVehicles = associatedIds.stream()
            .map(id -> vehicleRepository.findAllByOwnerId(id))
            .flatMap(Collection::stream)
            .toList();
    return relatedVehicles.stream().map((vehicle -> VehicleDTO.builder()
                    .plate(vehicle.getPlate())
                    .brand(vehicle.getBrand())
                    .model(vehicle.getModel())
                    .line(vehicle.getLine())
                    //.color(vehicle.getColor())
                    .isOwner(Objects.equals(vehicle.getOwnerId(), associatedIds.get(0)))
                    .build()
            )
    ).toList();

  }


}

