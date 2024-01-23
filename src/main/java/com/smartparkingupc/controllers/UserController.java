package com.smartparkingupc.controllers;

import com.smartparkingupc.controllers.dto.ConfidenceCircleDTO;
import com.smartparkingupc.controllers.dto.UserDTO;
import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.http.response.EntityResponse;
import com.smartparkingupc.services.IVehicleService;
import com.smartparkingupc.utils.EmailValidator;
import com.smartparkingupc.utils.ResponseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserService userService;

  @Autowired
  private IVehicleService vehicleService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody UserEntity user) {
    if (EmailValidator.isValid(user.getEmail())) {

      if (userService.findUserByEmail(user.getEmail()).isPresent()) return ResponseEntity.badRequest().build();

      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userService.saveUser(user);
      return EntityResponse.generateResponse(ResponseConstants.CREATED_USER_MESSAGE,
              HttpStatus.CREATED, "Created User");

    }
    return EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_CREATING_MESSAGE,
            HttpStatus.BAD_REQUEST, "Invalid Request");
  }

  @GetMapping("/vehicles")
  public ResponseEntity<Object> findAllUserAssociatedVehicles(@RequestAttribute String LoggedInUser) {

    Optional<UserEntity> optionalUser = userService.findUserByEmail(LoggedInUser);
    return optionalUser.<ResponseEntity<Object>>map(userEntity -> ResponseEntity.ok(vehicleService.findAssociatedVehicles(userEntity.getId())))
            .orElseGet(() -> EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_FINDING_VEHICLES, HttpStatus.NO_CONTENT, "Error while finding"));
  }

  @GetMapping("/profile")
  public ResponseEntity<Object> retrieveUserProfile() {
    return EntityResponse.generateResponse("User Profile", HttpStatus.OK, userService.findCurrentUser().get());
  }

  @GetMapping("/login")
  public ResponseEntity<?> findUserByEmail(@RequestAttribute("LoggedInUser") String email) {

    Optional<UserEntity> optionalUser = userService.findUserByEmail(email);
    if (optionalUser.isPresent()) {

      UserEntity user = optionalUser.get();
      List<VehicleDTO> vehicles = vehicleService.findAssociatedVehicles(user.getId());

      List<ConfidenceCircleDTO> confidenceCircleDTOS = user.getConfidenceCircle()
              .stream().map(confidenceCircleUser -> ConfidenceCircleDTO
                      .builder().email(confidenceCircleUser.getEmail())
                      .name(confidenceCircleUser.getName())
                      .build()
              ).toList();

      UserDTO userDTO = UserDTO.builder()
              .email(user.getEmail())
              .name(user.getName())
              .phoneNumber(user.getPhoneNumber())
              .confidenceCircle(confidenceCircleDTOS)
              .associatedVehicles(vehicles)
              .build();

      return ResponseEntity.ok(userDTO);

    }

    return EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_FINDING_USER, HttpStatus.NO_CONTENT, "Not found");

  }

}
