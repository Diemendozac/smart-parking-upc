package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.ConfidenceCircleRequest;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/confidence-request")
public class ConfidenceRequestController {

  @Autowired
  private IUserService userService;

  @PostMapping("/add")
  public ResponseEntity<?> saveConfidenceCircleRequest(
          @RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

    if(email.equals(loggedUserEmail)) return ResponseEntity.badRequest().build();
    Optional<UserEntity> loggedUserRequest = userService.findUserByEmail(loggedUserEmail);
    if (loggedUserRequest.isEmpty()) return ResponseEntity.notFound().build();

    Optional<UserEntity> requestedUser = userService.findUserByEmail(email);
    if (requestedUser.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    UserEntity loggedUser = requestedUser.get();
    if (loggedUser.getConfidenceRequest().size() >= 3) return ResponseEntity.badRequest().build();

    ConfidenceCircleRequest confidenceCircleRequest = createConfidenceCircleRequest(loggedUserRequest.get());
    loggedUser.getConfidenceRequest().add(confidenceCircleRequest);
    userService.saveUser(loggedUser);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteUserConfidenceRequest(
          @RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

    Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);
    if (loggedUserOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    UserEntity loggedUser = loggedUserOptional.get();
    loggedUser.getConfidenceRequest().removeIf(request -> request.getEmail().equals(email));
    userService.saveUser(loggedUser);

    return ResponseEntity.ok(loggedUser.getConfidenceRequest());
  }

  private ConfidenceCircleRequest createConfidenceCircleRequest(UserEntity user) {
    return ConfidenceCircleRequest.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();
  }
}
