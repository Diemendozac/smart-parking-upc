package com.smartparkingupc.controllers;

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

  @PostMapping()
  public ResponseEntity<?> saveUserConfidenceCircle(@RequestParam String email, @RequestHeader("LoggedInUser") String loggedUserEmail) {

    Optional<UserEntity> optionalUser = userService.findUserByEmail(email);
    Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);

    if (optionalUser.isPresent() && loggedUserOptional.isPresent()) {
      UserEntity userToAdd = optionalUser.get();
      UserEntity loggedUser = loggedUserOptional.get();
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

      return EntityResponse.generateResponse(ResponseConstants.CONFIDENCE_USER_ADDED, HttpStatus.OK, loggedUser.getConfidenceCircle());
    }

    return EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_ADDING_CONFIDENCE_USER, HttpStatus.BAD_REQUEST, "Bad Request");

  }

  @DeleteMapping()
  public ResponseEntity<?> deleteUserConfidenceCircle(@RequestParam String email, @RequestHeader("LoggedInUser") String loggedUserEmail) {

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
      return EntityResponse.generateResponse(
              ResponseConstants.CONFIDENCE_USER_DELETED,
              HttpStatus.OK,
              loggedUser.getConfidenceCircle()
      );

    }

    return EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_DELETING_USER, HttpStatus.BAD_REQUEST, "Not found user");

  }

}
