package com.smartparkingupc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparkingupc.http.request.UserCredential;
import com.smartparkingupc.security.JWTTokenUtil;
import com.smartparkingupc.services.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IUserService userService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JWTTokenUtil jwtTokenUtil;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper();
	}

	@Test
	@WithMockUser
	public void testCreateAuthenticationToken_Success() throws Exception {

		UserCredential userCredential = new UserCredential("test@example.com", "password");
		UserDetails userDetails = org.springframework.security.core.userdetails.User
						.withUsername("test@example.com")
						.password("password")
						.authorities("ROLE_USER")
						.roles("USER")
						.build();
		String token = "test-token";

		when(authenticationManager.authenticate(any())).thenReturn(null);
		when(userService.loadUserByEmail(anyString())).thenReturn(userDetails);
		when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn(token);

		mockMvc.perform(post("/authenticate")
										.contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(userCredential)))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.token").value(token));
	}

	@Test
	@WithMockUser
	public void testCreateAuthenticationToken_InvalidCredentials() throws Exception {
		UserCredential userCredential = new UserCredential("test@example.com", "password");

		when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("INVALID_CREDENTIALS"));

		mockMvc.perform(post("/authenticate")
										.contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(userCredential)))
						.andExpect(status().isUnauthorized());
		//.andExpect(jsonPath("$.message").value("Invalid credentials, please check details and try again."));
	}

}
