package com.smartparkingupc.controllers;

import com.smartparkingupc.controllers.dto.UserDTO;
import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.http.response.EntityResponse;
import com.smartparkingupc.utils.EmailValidator;
import com.smartparkingupc.utils.ResponseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserService userService;

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
  public Long findUserByVehicleRequestEmail(@RequestParam String email) {

    Optional<UserEntity> optionalUser = userService.findUserByEmail(email);
    if (optionalUser.isPresent()) {
      return (optionalUser.get().getId());
    }

    return (long) -1;
  }

  @GetMapping("/profile")
  public ResponseEntity<Object> retrieveUserProfile(){
    return EntityResponse.generateResponse("User Profile", HttpStatus.OK, userService.findCurrentUser().get());
  }

  @GetMapping("/login")
  public ResponseEntity<?> findUserByEmail(@RequestHeader("LoggedInUser") String email) {

    Optional<UserEntity> optionalUser = userService.findUserByEmail(email);
    if (optionalUser.isPresent()) {

      UserEntity user = optionalUser.get();
      List<Long> associatedUsers = new ArrayList<>();
      associatedUsers.add(user.getId());
      user.getConfidenceCircle().forEach((confidenceCircleUser -> associatedUsers.add(confidenceCircleUser.getId())));
      List<VehicleDTO> vehicles = userService.getAllUserVehiclesById(associatedUsers);

      UserDTO userDTO = UserDTO.builder()
              .email(user.getEmail())
              .name(user.getName())
              .phoneNumber(user.getPhoneNumber())
              .associatedVehicles(vehicles)
              .build();

      return EntityResponse.generateResponse(
              ResponseConstants.FOUND_USER_MESSAGE, HttpStatus.OK, userDTO);

    }

    return EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_FINDING_MESSAGE, HttpStatus.NO_CONTENT, "Not found");

  }


}
