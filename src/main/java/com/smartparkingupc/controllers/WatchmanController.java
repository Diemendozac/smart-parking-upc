package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.Ticket;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.http.response.UserEntityByWatchmanResponse;
import com.smartparkingupc.services.ITicketService;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.services.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/watchman")
public class WatchmanController {

  @Autowired private IUserService userService;
  @Autowired private IVehicleService vehicleService;
  @Autowired private ITicketService ticketService;

  @GetMapping("/related-users")
  public ResponseEntity<?> findAllVehicleRelatedUsersByPlate(@RequestParam String plate) {

    Optional<Vehicle> optVehicle = vehicleService.findVehicleByPlate(plate);
    if (optVehicle.isEmpty()) return ResponseEntity.noContent().build();
    Long ownerId = optVehicle.get().getOwnerId();
    List<UserEntityByWatchmanResponse> vehicleRelatedUsers =
        userService.getVehicleRelatedUsers(ownerId);
    return ResponseEntity.ok(vehicleRelatedUsers);
  }

  @GetMapping("/parked-vehicles")
  public ResponseEntity<?> findAllParkedVehicles() {
    return ResponseEntity.ok(vehicleService.findAllParkedVehicleDTOs());
  }

  @PatchMapping("/switch-state")
  public ResponseEntity<?> switchParkingStateByPlate(
      @RequestParam String plate, @RequestParam String watchmanSelectedUser) {
    Optional<Vehicle> optVehicle = vehicleService.findVehicleByPlate(plate);
    if (optVehicle.isEmpty()) return ResponseEntity.notFound().build();
    Vehicle vehicle = optVehicle.get();
    Optional<UserEntity> optionalUser = userService.findUserById(vehicle.getOwnerId());
    if (optionalUser.isEmpty()) return ResponseEntity.notFound().build();
    UserEntity user = optionalUser.get();

    boolean isWatchmanSelectedUserPresentInConfidenceCircle =
        user.getConfidenceCircle().stream()
            .anyMatch(
                confidenceCircleUser ->
                    Objects.equals(confidenceCircleUser.getEmail(), watchmanSelectedUser));

    if (!isWatchmanSelectedUserPresentInConfidenceCircle
        && !Objects.equals(watchmanSelectedUser, user.getEmail()))
      return ResponseEntity.notFound().build();

    boolean setParkedStatus = !vehicle.isParked();

    Ticket ticket =
        Ticket.builder()
            .vehiclePlate(vehicle.getPlate())
            .watchmanSelectedUser(watchmanSelectedUser)
            .userOwnerEmail(user.getEmail())
            .createdAt(LocalDateTime.now())
            .isGettingIn(setParkedStatus)
            .build();
    vehicle.setParked(setParkedStatus);
    vehicleService.save(vehicle, vehicle.getOwnerId());
    ticketService.saveTicket(ticket);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/kick")
  public ResponseEntity<?> kickParkedVehicle(@RequestParam String plate) {
    return vehicleService.kickOffVehicle(plate) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }

  @GetMapping("/kick-all")
  public ResponseEntity<?> kickAllParkedVehicle() {
    List<Vehicle> vehiclesToKick = vehicleService.findAllParkedVehicles();
    vehiclesToKick.forEach(
        vehicle -> {
          vehicle.setParked(false);
          vehicleService.save(vehicle, vehicle.getOwnerId());
        });
    return ResponseEntity.ok(vehicleService.findAllParkedVehicleDTOs());
  }


}
