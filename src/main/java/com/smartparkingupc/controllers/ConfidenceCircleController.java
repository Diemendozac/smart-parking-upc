package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.ConfidenceCircleRequest;
import com.smartparkingupc.entities.ConfidenceCircleUser;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/confidence-circle")
public class ConfidenceCircleController {

	@Autowired
	private IUserService userService;

	@PostMapping("/add")
	public ResponseEntity<?> saveUserConfidenceCircle(
					@RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

		Optional<UserEntity> optionalRequestUser = userService.findUserByEmail(email);
		if (optionalRequestUser.isEmpty()) return ResponseEntity.notFound().build();

		Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);
		if (loggedUserOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		UserEntity loggedUser = loggedUserOptional.get();
		if (!findUserRequest(loggedUser.getConfidenceRequest(), email))
			return ResponseEntity.badRequest().build();

		if (loggedUser.getConfidenceCircle().size() > 2) return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();;

		ConfidenceCircleUser confidenceCircleUser = createConfidenceCircleUser(optionalRequestUser.get());
		loggedUser.getConfidenceCircle().add(confidenceCircleUser);
		userService.saveUser(loggedUser);

		return ResponseEntity.ok(loggedUser.getConfidenceCircle());
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUserConfidenceCircle(
					@RequestParam String email, @RequestAttribute("LoggedInUser") String loggedUserEmail) {

		Optional<UserEntity> loggedUserOptional = userService.findUserByEmail(loggedUserEmail);
		if (loggedUserOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		UserEntity loggedUser = loggedUserOptional.get();
		loggedUser.getConfidenceCircle().removeIf(user -> user.getEmail().equals(email));
		userService.saveUser(loggedUser);

		return ResponseEntity.ok(loggedUser.getConfidenceCircle());
	}

	private boolean findUserRequest(
					List<ConfidenceCircleRequest> confidenceCircleRequests, String email) {
		return confidenceCircleRequests.stream().anyMatch(requestUser -> requestUser.getEmail().equals(email));
	}

	private ConfidenceCircleUser createConfidenceCircleUser(UserEntity user) {
		return ConfidenceCircleUser.builder()
						.id(user.getId())
						.name(user.getName())
						.email(user.getEmail())
						.phoneNumber(user.getPhoneNumber())
						.build();
	}
}
