package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.ConfidenceCircleRequest;
import com.smartparkingupc.entities.ConfidenceCircleUser;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.security.JWTTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConfidenceCircleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConfidenceCircleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IUserService userService;

	@MockBean
	private JWTTokenUtil jwtTokenUtil;

	private UserEntity loggedUser;
	private UserEntity requestUser;

	@BeforeEach
	public void setUp() {
		loggedUser = new UserEntity();
		loggedUser.setEmail("loggedUser@example.com");
		loggedUser.setConfidenceCircle(new ArrayList<>());
		loggedUser.setConfidenceRequest(new ArrayList<>());

		requestUser = new UserEntity();
		requestUser.setEmail("requestUser@example.com");
	}

	@Test
	public void testSaveUserConfidenceCircle_Success() throws Exception {
		ConfidenceCircleRequest request = new ConfidenceCircleRequest();
		request.setEmail("requestUser@example.com");
		loggedUser.getConfidenceRequest().add(request);

		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));
		when(userService.findUserByEmail("requestUser@example.com")).thenReturn(Optional.of(requestUser));

		mockMvc.perform(post("/confidence-circle/add")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	}

	@Test
	public void testSaveUserConfidenceCircle_UserNotFound() throws Exception {
		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));
		when(userService.findUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

		mockMvc.perform(post("/confidence-circle/add")
										.param("email", "nonexistent@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound());
	}

	@Test
	public void testSaveUserConfidenceCircle_RequestNotFound() throws Exception {
		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));
		when(userService.findUserByEmail("requestUser@example.com")).thenReturn(Optional.of(requestUser));

		mockMvc.perform(post("/confidence-circle/add")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest());
	}

	@Test
	public void testSaveUserConfidenceCircle_TooManyUsers() throws Exception {
		loggedUser.getConfidenceCircle().add(new ConfidenceCircleUser());
		loggedUser.getConfidenceCircle().add(new ConfidenceCircleUser());
		loggedUser.getConfidenceCircle().add(new ConfidenceCircleUser());

		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));
		when(userService.findUserByEmail("requestUser@example.com")).thenReturn(Optional.of(requestUser));

		mockMvc.perform(post("/confidence-circle/add")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotAcceptable());
	}

	@Test
	public void testDeleteUserConfidenceCircle_Success() throws Exception {
		ConfidenceCircleUser circleUser = new ConfidenceCircleUser();
		circleUser.setEmail("requestUser@example.com");
		loggedUser.getConfidenceCircle().add(circleUser);

		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));

		mockMvc.perform(delete("/confidence-circle/delete")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	}

	@Test
	public void testDeleteUserConfidenceCircle_UserNotFound() throws Exception {
		when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(delete("/confidence-circle/delete")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isUnauthorized());
	}
}
