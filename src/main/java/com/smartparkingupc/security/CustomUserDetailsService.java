package com.smartparkingupc.security;

import com.smartparkingupc.config.CustomUserDetails;
import com.smartparkingupc.http.request.UserCredential;
import com.smartparkingupc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<UserCredential> foundUser = userRepository.findUserEntityByEmail(email);
    return foundUser.map(CustomUserDetails::new).orElseThrow(()-> new UsernameNotFoundException("User not found with name: " + email));

  }
}
