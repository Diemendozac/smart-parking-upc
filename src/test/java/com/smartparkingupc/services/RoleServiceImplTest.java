package com.smartparkingupc.services;

import com.smartparkingupc.entities.Role;
import com.smartparkingupc.repositories.RoleRepository;
import com.smartparkingupc.services.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private RoleServiceImpl roleService;

	private Role role;

	@BeforeEach
	void setUp() {
		role = new Role();
		role.setName("ROLE_USER");
	}

	@Test
	void testSaveRole() {
		when(roleRepository.save(role)).thenReturn(role);

		Role savedRole = roleService.save(role);

		assertNotNull(savedRole);
		assertEquals("ROLE_USER", savedRole.getName());
		verify(roleRepository, times(1)).save(role);
	}

	@Test
	void testFindAllRole() {
		when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));

		List<Role> roles = roleService.findAllRole();

		assertNotNull(roles);
		assertEquals(1, roles.size());
		verify(roleRepository, times(1)).findAll();
	}

	@Test
	void testFindDefaultRole() {
		when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));

		Role defaultRole = roleService.findDefaultRole();

		assertNotNull(defaultRole);
		assertEquals("ROLE_USER", defaultRole.getName());
		verify(roleRepository, times(1)).findAll();
	}
}
