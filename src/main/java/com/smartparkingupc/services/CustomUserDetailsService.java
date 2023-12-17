package com.smartparkingupc.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.UserRole;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.repositories.IUserRoleRepository;
import com.smartparkingupc.security.SecurityPrincipal;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
  //private static final Logger LOG = LoggerFactory.getLogger(WUserService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private IUserRoleRepository userRoleRepository;

  @Autowired
  private IUserRoleRepository roleService;

  @Override
  public UserDetails loadUserByUsername(String email) {
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

  public Optional<UserEntity> findByUsername(String username) {
    return userRepository.findByEmail(username);
  }

  public UserEntity findCurrentUser() {
    return userRepository.findById(SecurityPrincipal.getInstance().getLoggedInPrincipal().getId()).get();

  }

  public List<UserRole> findAllCurrentUserRole() {
    return userRoleRepository.findAllByUserId(SecurityPrincipal.getInstance().getLoggedInPrincipal().getId());

  }


}
