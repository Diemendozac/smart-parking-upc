package com.smartparkingupc.services;

import com.smartparkingupc.entities.Role;
import com.smartparkingupc.repositories.RoleRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl {

	@Autowired
	private RoleRepository roleReposirory;

	public Role save(Role role) {
		return roleReposirory.save(role);
	}

	public List<Role> findAllRole() {
		return (List<Role>) roleReposirory.findAll();
	}

	public Role findDefaultRole() {
		return findAllRole().stream().findFirst().orElse(null);
	}

	public Role findRoleByName(String role) {
		return findAllRole().stream().filter(r -> r.getName().equals(role)).findFirst().orElse(null);
	}

}
