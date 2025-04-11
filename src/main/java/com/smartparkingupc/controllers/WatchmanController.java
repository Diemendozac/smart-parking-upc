package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.Ticket;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.http.request.TicketCreationRequest;
import com.smartparkingupc.http.response.RelatedUsersResponse;
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
    Vehicle vehicle = optVehicle.get();
    Long ownerId = vehicle.getOwnerId();
    List<UserEntityByWatchmanResponse> vehicleRelatedUsers =
        userService.getVehicleRelatedUsers(ownerId);
    RelatedUsersResponse response = RelatedUsersResponse.builder()
            .users(vehicleRelatedUsers)
            .isParked(vehicle.isParked())
            .build();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/parked-vehicles")
  public ResponseEntity<?> findAllParkedVehicles() {
    return ResponseEntity.ok(vehicleService.findAllParkedVehicleDTOs());
  }

  @PatchMapping("/switch-state")
  public ResponseEntity<?> switchParkingStateByPlate(
          @RequestBody TicketCreationRequest request) {
    // Validar que la placa y usuario seleccionado no sean null
    if (request.getPlate() == null || request.getWatchmanSelectedUser() == null) {
      return ResponseEntity.badRequest().build();
    }

    Optional<Vehicle> optVehicle = vehicleService.findVehicleByPlate(request.getPlate());
    if (optVehicle.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Vehicle vehicle = optVehicle.get();

    Optional<UserEntity> optionalUser = userService.findUserById(vehicle.getOwnerId());
    if (optionalUser.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    UserEntity user = optionalUser.get();

    // Validar que confidence circle no sea null
    if (user.getConfidenceCircle() == null) {
      // Solo permitir si el usuario seleccionado es el propietario
      if (!Objects.equals(request.getWatchmanSelectedUser(), user.getEmail())) {
        return ResponseEntity.notFound().build();
      }
    } else {
      // Verificar si el usuario seleccionado está en el círculo de confianza o es el propietario
      boolean isWatchmanSelectedUserPresentInConfidenceCircle =
              user.getConfidenceCircle().stream()
                      .anyMatch(
                              confidenceCircleUser ->
                                      Objects.equals(confidenceCircleUser.getEmail(), request.getWatchmanSelectedUser()));

      if (!isWatchmanSelectedUserPresentInConfidenceCircle
              && !Objects.equals(request.getWatchmanSelectedUser(), user.getEmail())) {
        return ResponseEntity.notFound().build();
      }
    }

    boolean setParkedStatus = !vehicle.isParked();

    Ticket ticket =
            Ticket.builder()
                    .vehiclePlate(vehicle.getPlate())
                    .watchmanSelectedUser(request.getWatchmanSelectedUser())
                    .userOwnerEmail(user.getEmail())
                    .createdAt(LocalDateTime.now())
                    .isGettingIn(setParkedStatus)
                    .build();

    vehicle.setParked(setParkedStatus);
    vehicleService.switchState(vehicle);
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
