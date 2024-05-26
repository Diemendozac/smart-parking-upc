package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.ConfidenceCircleRequest;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConfidenceRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ConfidenceRequestControllerTest {

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
		loggedUser.setConfidenceRequest(new ArrayList<>());

		requestUser = new UserEntity();
		requestUser.setEmail("requestUser@example.com");
		requestUser.setConfidenceRequest(new ArrayList<>());
	}

	@Test
	public void testSaveConfidenceCircleRequest_Success() throws Exception {
		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));
		when(userService.findUserByEmail("requestUser@example.com")).thenReturn(Optional.of(requestUser));

		mockMvc.perform(post("/confidence-request/add")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testSaveConfidenceCircleRequest_SameEmail() throws Exception {
		mockMvc.perform(post("/confidence-request/add")
										.param("email", "loggedUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	public void testSaveConfidenceCircleRequest_UserNotFound() throws Exception {
		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));
		when(userService.findUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

		mockMvc.perform(post("/confidence-request/add")
										.param("email", "nonexistent@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void testSaveConfidenceCircleRequest_TooManyRequests() throws Exception {
		loggedUser.getConfidenceRequest().add(new ConfidenceCircleRequest());
		loggedUser.getConfidenceRequest().add(new ConfidenceCircleRequest());
		loggedUser.getConfidenceRequest().add(new ConfidenceCircleRequest());

		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));
		when(userService.findUserByEmail("requestUser@example.com")).thenReturn(Optional.of(requestUser));

		mockMvc.perform(post("/confidence-request/add")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	public void testDeleteUserConfidenceRequest_Success() throws Exception {
		ConfidenceCircleRequest request = new ConfidenceCircleRequest();
		request.setEmail("requestUser@example.com");
		loggedUser.getConfidenceRequest().add(request);

		when(userService.findUserByEmail("loggedUser@example.com")).thenReturn(Optional.of(loggedUser));

		mockMvc.perform(delete("/confidence-request/delete")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testDeleteUserConfidenceRequest_UserNotFound() throws Exception {
		when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(delete("/confidence-request/delete")
										.param("email", "requestUser@example.com")
										.requestAttr("LoggedInUser", "loggedUser@example.com")
										.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isUnauthorized());
	}
}
