package com.smartparkingupc.services;

import com.smartparkingupc.entities.ConfidenceCircleUser;
import com.smartparkingupc.entities.Role;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.UserRole;
import com.smartparkingupc.http.response.UserEntityByWatchmanResponse;
import com.smartparkingupc.repositories.IUserRoleRepository;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.services.impl.RoleServiceImpl;
import com.smartparkingupc.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private IUserRoleRepository userRoleRepository;

	@Mock
	private RoleServiceImpl roleService;

	@InjectMocks
	private UserServiceImpl userService;

	private UserEntity user;
	private UserRole userRole;

	@BeforeEach
	void setUp() {
		user = new UserEntity();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setPassword("password");
		user.setName("Test User");
		Role testRole = new Role();
		testRole.setName("TEST_ROLE");
		testRole.setId(5L);

		userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(testRole);
	}

	@Test
	void testFindUserById() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		Optional<UserEntity> result = userService.findUserById(1L);

		assertTrue(result.isPresent());
		assertEquals("test@example.com", result.get().getEmail());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void testLoadUserByEmail() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(userRoleRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(userRole));

		UserDetails userDetails = userService.loadUserByEmail("test@example.com");

		assertNotNull(userDetails);
		assertEquals("test@example.com", userDetails.getUsername());
		assertEquals(1, userDetails.getAuthorities().size());
		verify(userRepository, times(1)).findByEmail("test@example.com");
		verify(userRoleRepository, times(1)).findAllByUserId(1L);
	}

	@Test
	void testLoadUserByEmailNotFound() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

		UserDetails userDetails = userService.loadUserByEmail("test@example.com");

		assertNull(userDetails);
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void testFindUserByEmail() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		Optional<UserEntity> result = userService.findUserByEmail("test@example.com");

		assertTrue(result.isPresent());
		assertEquals("test@example.com", result.get().getEmail());
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void testSaveUser() {
		Role testRole = new Role();
		testRole.setName("TEST_ROLE");
		testRole.setId(5L);

		when(userRepository.save(user)).thenReturn(user);
		when(roleService.findDefaultRole()).thenReturn(testRole);
		when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

		userService.saveUser(user);

		verify(userRepository, times(1)).save(user);
		verify(userRoleRepository, times(1)).save(any(UserRole.class));
	}

	@Test
	void testGetVehicleRelatedUsers() {
		ConfidenceCircleUser confidenceCircleUser = new ConfidenceCircleUser();
		confidenceCircleUser.setId(2L);
		confidenceCircleUser.setName("Confidence User");
		confidenceCircleUser.setEmail("confidence@example.com");
		confidenceCircleUser.setPhoneNumber("123456789");
		confidenceCircleUser.setPhotoUrl("photo_url");

		List<ConfidenceCircleUser> confidenceCircleUsers = new ArrayList<>();
		confidenceCircleUsers.add(confidenceCircleUser);
		user.setConfidenceCircle(confidenceCircleUsers);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		List<UserEntityByWatchmanResponse> result = userService.getVehicleRelatedUsers(1L);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Test User", result.get(0).getName());
		assertEquals("Confidence User", result.get(1).getName());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void testGetVehicleRelatedUsersOwnerNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		List<UserEntityByWatchmanResponse> result = userService.getVehicleRelatedUsers(1L);

		assertNull(result);
		verify(userRepository, times(1)).findById(1L);
	}
}
