package com.smartparkingupc.services;

import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.UserRole;
import com.smartparkingupc.repositories.IUserRoleRepository;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private VehicleRepository vehicleRepository;
  @Autowired
  private IUserRoleRepository userRoleRepository;

  public static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public List<UserEntity> findAll() {
    return null;
  }

  @Override
  public Optional<UserEntity> findUserById(Long id) {

    return userRepository.findById(id);
  }

  @Override
  public UserDetails loadUserByEmail(String email) {

    Optional<UserEntity> optUser = userRepository.findByEmail(email);
    if (optUser.isPresent()) {
      UserEntity user = optUser.get();
      List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());

      Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

      userRoles.forEach(userRole -> {
        authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
      });

      return new org.springframework.security.core.userdetails.User(user.getUsername(),
              user.getPassword(), authorities);
    }
    return null;
  }

  @Override
  public Optional<UserEntity> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public void saveUser(UserEntity user) {
    userRepository.save(user);

  }

  @Override
  public void saveAndEncode(UserEntity user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);

  }

  @Override
  public List<VehicleDTO> getAllUserVehiclesById(List<Long> associatedIds) {

    /*List<VehicleByUserEntityResponse> vehiclesResponse = vehicleRepository.findAllAssociatedVehicles(associatedIds);

    return vehiclesResponse.stream()
            .map((vehicle -> VehicleDTO.builder()
                            .plate(vehicle.getPlate())
                            .brand(vehicle.getBrand())
                            .model(vehicle.getModel())
                            .line(vehicle.getLine())
                            //.color(vehicle.getColor())
                            .isOwner(Objects.equals(vehicle.getOwnerId(), associatedIds.get(0)))
                            .build()
                    )
            ).toList();*/

    return null;
  }

}
