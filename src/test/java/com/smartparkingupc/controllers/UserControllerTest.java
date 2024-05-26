package com.smartparkingupc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.ConfidenceCircleRequest;
import com.smartparkingupc.entities.ConfidenceCircleUser;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.security.JWTTokenUtil;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.services.IVehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IUserService userService;

	@MockBean
	private IVehicleService vehicleService;

	@MockBean
	private PasswordEncoder passwordEncoder;
	@MockBean
	private JWTTokenUtil jwtTokenUtil;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testRegisterUser_Success() throws Exception {
		UserEntity user = new UserEntity();
		user.setEmail("test@unicesar.edu.co");
		user.setPassword("password");

		when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

		mockMvc.perform(post("/user/register")
										.contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(user)))
						.andExpect(status().isCreated());
						//.andExpect(jsonPath("$.message").value(ResponseConstants.CREATED_USER_MESSAGE));
	}

	@Test
	public void testRegisterUser_EmailInvalid() throws Exception {
		UserEntity user = new UserEntity();
		user.setEmail("invalid-email");
		user.setPassword("password");

		mockMvc.perform(post("/user/register")
										.contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(user)))
						.andExpect(status().isBadRequest());
						//.andExpect(jsonPath("$.message").value(ResponseConstants.ISSUE_WHILE_CREATING_MESSAGE));
	}

	@Test
	public void testFindAllUserAssociatedVehicles_Success() throws Exception {
		UserEntity user = new UserEntity();
		user.setEmail("test@example.com");
		user.setId(1L);
		List<VehicleDTO> vehicles = Collections.emptyList();

		when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user));
		when(vehicleService.findAssociatedVehicles(any(Long.class))).thenReturn(vehicles);

		mockMvc.perform(get("/user/vehicles")
										.requestAttr("LoggedInUser", "test@example.com"))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$").isArray());
	}

	@Test
	public void testFindAllUserAssociatedVehicles_NoContent() throws Exception {
		when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(get("/user/vehicles")
										.requestAttr("LoggedInUser", "test@example.com"))
						.andExpect(status().isNoContent());
	}

	@Test
	public void testFindUserByEmail_Success() throws Exception {
		UserEntity user = new UserEntity();
		user.setEmail("test@example.com");
		user.setId(1L);
		user.setConfidenceCircle(new ArrayList<ConfidenceCircleUser>());
		user.setConfidenceRequest(new ArrayList<ConfidenceCircleRequest>());

		when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user));
		when(vehicleService.findAssociatedVehicles(any(Long.class))).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/user/login")
										.requestAttr("LoggedInUser", "test@example.com"))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.email").value("test@example.com"));
	}

	@Test
	public void testFindUserByEmail_NoContent() throws Exception {
		when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(get("/user/login")
										.requestAttr("LoggedInUser", "test@example.com"))
						.andExpect(status().isNoContent());
	}
}
