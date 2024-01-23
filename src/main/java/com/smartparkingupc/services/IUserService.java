package com.smartparkingupc.services;

import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.http.response.UserEntityByWatchmanResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface IUserService {

  List<UserEntity> findAll();
  Optional<UserEntity> findUserById(Long id);

  UserDetails loadUserByEmail(String email);

  Optional<UserEntity> findUserByEmail(String email);
  void saveUser(UserEntity user);
  Optional<UserEntity> findCurrentUser();

  List<UserEntityByWatchmanResponse> getVehicleRelatedUsers(Long ownerId);


}
