package com.smartparkingupc.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.repositories.IUserRoleRepository;

import jakarta.transaction.Transactional;

import static com.smartparkingupc.services.impl.UserServiceImpl.getUserDetails;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  @Autowired private IUserRoleRepository userRoleRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    return getUserDetails(email, userRepository, userRoleRepository);
  }
}
