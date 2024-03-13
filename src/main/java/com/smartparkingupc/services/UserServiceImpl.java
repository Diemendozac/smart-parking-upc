package com.smartparkingupc.services;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.UserRole;
import com.smartparkingupc.http.response.UserEntityByWatchmanResponse;
import com.smartparkingupc.repositories.IUserRoleRepository;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.security.SecurityPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements IUserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleServiceImpl roleService;

  @Autowired
  private IUserRoleRepository userRoleRepository;


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

      userRoles.forEach(userRole -> authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName())));

      return new org.springframework.security.core.userdetails.User(user.getEmail(),
              user.getPassword(), authorities);
    }
    return null;
  }

  @Override
  public Optional<UserEntity> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public UserEntity saveUser(UserEntity user) {
    UserEntity savedUser = userRepository.save(user);

    UserRole userRole = UserRole.builder()
    .user(savedUser)
    .role(roleService.findDefaultRole()).build();
    UserRole roleSaved = userRoleRepository.save(userRole);


    return savedUser ;
  }

  public Optional<UserEntity> findCurrentUser() {
    return userRepository.findById(SecurityPrincipal.getInstance().getLoggedInPrincipal().getId());

  }

  @Override
  public List<UserEntityByWatchmanResponse> getVehicleRelatedUsers(Long ownerId) {
    Optional<UserEntity> optOwner = userRepository.findById(ownerId);
    if (optOwner.isEmpty()) return null;
    UserEntity owner = optOwner.get();
    UserEntityByWatchmanResponse ownerByWatchmanResponse = UserEntityByWatchmanResponse.builder()
            .name(owner.getName())
            .email(owner.getEmail())
            .phoneNumber(owner.getPhoneNumber())
            .photoUrl(owner.getPhotoUrl()).build();
    List<UserEntityByWatchmanResponse> relatedUsers = new ArrayList<>();
    relatedUsers.add(ownerByWatchmanResponse);
    owner.getConfidenceCircle().forEach(confidenceCircleUser -> relatedUsers.add(UserEntityByWatchmanResponse
            .builder().name(confidenceCircleUser.getName())
            .phoneNumber(confidenceCircleUser.getPhoneNumber())
            .photoUrl(confidenceCircleUser.getPhotoUrl())
            .email(confidenceCircleUser.getEmail()).build()));
    return relatedUsers;
  }

}
