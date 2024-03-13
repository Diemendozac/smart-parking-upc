package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.ConfidenceCircleRequests;
import com.smartparkingupc.entities.ConfidenceCircleUser;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.http.response.EntityResponse;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.utils.ResponseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/confidence-circle")
public class ConfidenceCircleController {

  @Autowired
  private IUserService userService;

  @PostMapping("/add")
  public ResponseEntity<?> saveUserConfidenceCircle(@RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

    Optional<UserEntity> optionalRequestUser = userService.findUserByEmail(email);
    if(optionalRequestUser.isEmpty()) return ResponseEntity.notFound().build();
    UserEntity loggedUser = optionalRequestUser.get();
    if(findUserRequest(loggedUser.getConfidenceRequest(), email)) return ResponseEntity.badRequest().build();
    Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);

    if (loggedUserOptional.isPresent()) {
      UserEntity userToAdd = optionalRequestUser.get();
      List<ConfidenceCircleUser> confidenceCircle = loggedUser.getConfidenceCircle();
      if (confidenceCircle.size() > 2) return ResponseEntity.badRequest().build();

      ConfidenceCircleUser confidenceCircleUser = ConfidenceCircleUser.builder()
              .id(userToAdd.getId())
              .name(userToAdd.getName())
              .email(userToAdd.getEmail())
              .phoneNumber(userToAdd.getPhoneNumber())
              .build();

      confidenceCircle.add(confidenceCircleUser);
      loggedUser.setConfidenceCircle(confidenceCircle);
      userService.saveUser(loggedUser);

      return ResponseEntity.ok(loggedUser.getConfidenceCircle());
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteUserConfidenceCircle(@RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

    Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);

    if (loggedUserOptional.isPresent()) {
      UserEntity loggedUser = loggedUserOptional.get();

      List<ConfidenceCircleUser> confidenceCircleUserList = loggedUser.getConfidenceCircle()
              .stream()
              .filter(
                      (confidenceCircleUser -> !confidenceCircleUser.getEmail().equals(email))
              ).toList();
      ArrayList<ConfidenceCircleUser> confidenceCircle = new ArrayList<>(confidenceCircleUserList);
      loggedUser.setConfidenceCircle(confidenceCircle);
      userService.saveUser(loggedUser);
      return ResponseEntity.ok(loggedUser.getConfidenceCircle());

    }

    return EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_DELETING_USER, HttpStatus.BAD_REQUEST, "Not found user");

  }

  private boolean findUserRequest(List<ConfidenceCircleRequests> confidenceCircleRequests, String email) {

    return confidenceCircleRequests.stream().anyMatch( (requestUser -> requestUser.getEmail().equals(email) ));

  }

}
