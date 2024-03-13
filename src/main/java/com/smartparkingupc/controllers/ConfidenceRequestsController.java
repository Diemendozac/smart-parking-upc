package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.ConfidenceCircleRequests;
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
@RequestMapping("confidence-request")
public class ConfidenceRequestsController {

  @Autowired
  private IUserService userService;

  @PostMapping("/add")
  public ResponseEntity<?> saveConfidenceCircleRequest(@RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

    Optional<UserEntity> optionalRequestUser = userService.findUserByEmail(email);
    if(optionalRequestUser.isEmpty()) return ResponseEntity.notFound().build();

    Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);

    if (loggedUserOptional.isPresent()) {
      UserEntity userToAdd = optionalRequestUser.get();
      UserEntity loggedUser = loggedUserOptional.get();
      List<ConfidenceCircleRequests> confidenceRequests = loggedUser.getConfidenceRequest();
      if (confidenceRequests.size() > 3) return ResponseEntity.badRequest().build();

      ConfidenceCircleRequests confidenceCircleRequest = ConfidenceCircleRequests.builder()
              .id(userToAdd.getId())
              .name(userToAdd.getName())
              .email(userToAdd.getEmail())
              .build();

      confidenceRequests.add(confidenceCircleRequest);
      loggedUser.setConfidenceRequest(confidenceRequests);
      userService.saveUser(loggedUser);

      return ResponseEntity.ok(loggedUser.getConfidenceRequest());
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteUserConfidenceRequest(@RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

    Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);

    if (loggedUserOptional.isPresent()) {
      UserEntity loggedUser = loggedUserOptional.get();

      List<ConfidenceCircleRequests> confidenceCircleUserList = loggedUser.getConfidenceRequest()
              .stream()
              .filter(
                      (confidenceCircleUser -> !confidenceCircleUser.getEmail().equals(email))
              ).toList();
      ArrayList<ConfidenceCircleRequests> confidenceCircle = new ArrayList<>(confidenceCircleUserList);
      loggedUser.setConfidenceRequest(confidenceCircle);
      userService.saveUser(loggedUser);
      return ResponseEntity.ok(loggedUser.getConfidenceRequest());

    }

    return EntityResponse.generateResponse(ResponseConstants.ISSUE_WHILE_DELETING_USER, HttpStatus.BAD_REQUEST, "Not found user");

  }


}
