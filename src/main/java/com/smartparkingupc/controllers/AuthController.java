package com.smartparkingupc.controllers;

import com.smartparkingupc.http.request.UserCredential;
import com.smartparkingupc.http.response.EntityResponse;
import com.smartparkingupc.security.JWTTokenUtil;
import com.smartparkingupc.security.dto.AuthenticationResponse;
import com.smartparkingupc.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Autowired
  private IUserService userService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JWTTokenUtil jwtTokenUtil;

  @PostMapping("/authenticate")
  public ResponseEntity<Object> createAuthenticationToken(@RequestBody UserCredential authenticationRequest) {

    try {
      authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
    } catch (Exception e) {
      return EntityResponse.generateResponse("Authentication", HttpStatus.UNAUTHORIZED,
              "Invalid credentials, please check details and try again.");
    }
    final UserDetails userDetails = userService.loadUserByEmail(authenticationRequest.getEmail());

    final String token = jwtTokenUtil.generateToken(userDetails);
    final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

    return EntityResponse.generateResponse("Authentication", HttpStatus.OK,
            new AuthenticationResponse(token, refreshToken));

  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }catch(Exception e) {
      throw new Exception("INVALID_CREDENTIALS", e.getCause());

    }
  }
}
