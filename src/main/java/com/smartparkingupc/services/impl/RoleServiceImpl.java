package com.smartparkingupc.services.impl;

import com.smartparkingupc.entities.Role;
import com.smartparkingupc.repositories.RoleRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl {

  @Autowired private RoleRepository roleRepository;

  public Role save(Role role) {
    return roleRepository.save(role);
  }

  public List<Role> findAllRole() {
    return (List<Role>) roleRepository.findAll();
  }

  public Role findDefaultRole() {
    // List<Role> roleList = findAllRole();
    return findAllRole().stream().findFirst().orElse(null);
  }
}
