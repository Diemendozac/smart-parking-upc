package com.smartparkingupc.services;

import com.smartparkingupc.entities.Role;
import com.smartparkingupc.repositories.IUserRoleRepository;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private IUserRoleRepository userRoleRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	private UserEntity user;
	private UserRole userRole;

	@BeforeEach
	void setUp() {
		user = new UserEntity();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setPassword("password");
		Role testRole = new Role();
		testRole.setName("ROLE_USER");

		userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(testRole);
	}

	@Test
	void testLoadUserByUsername() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(userRoleRepository.findAllByUserId(1L)).thenReturn(List.of(userRole));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

		assertNotNull(userDetails);
		assertEquals("test@example.com", userDetails.getUsername());
		assertEquals(1, userDetails.getAuthorities().size());
		verify(userRepository, times(1)).findByEmail("test@example.com");
		verify(userRoleRepository, times(1)).findAllByUserId(1L);
	}

	@Test
	void testLoadUserByUsernameNotFound() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

		assertNull(userDetails);
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}
}
