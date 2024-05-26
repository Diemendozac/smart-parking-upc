package com.smartparkingupc.controllers;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.http.response.UserEntityByWatchmanResponse;
import com.smartparkingupc.security.JWTTokenUtil;
import com.smartparkingupc.services.ITicketService;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.services.IVehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = WatchmanController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WatchmanControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IUserService userService;

	@MockBean
	private IVehicleService vehicleService;
	@MockBean
	private ITicketService ticketService;
	@MockBean
	private JWTTokenUtil jwtTokenUtil;

	@Test
	@WithMockUser
	public void testFindAllVehicleRelatedUsersByPlate() throws Exception {
		Vehicle vehicle = new Vehicle();
		vehicle.setOwnerId(1L);
		UserEntityByWatchmanResponse userResponse = new UserEntityByWatchmanResponse();
		userResponse.setEmail("test@example.com");
		List<UserEntityByWatchmanResponse> userResponses = List.of(userResponse);

		when(vehicleService.findVehicleByPlate(anyString())).thenReturn(Optional.of(vehicle));
		when(userService.getVehicleRelatedUsers(anyLong())).thenReturn(userResponses);

		mockMvc.perform(get("/watchman/related-users")
										.param("plate", "ABC123"))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].email").value("test@example.com"));
	}

	@Test
	@WithMockUser
	public void testFindAllParkedVehicles() throws Exception {
		mockMvc.perform(get("/watchman/parked-vehicles"))
						.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testSwitchParkingStateByPlate() throws Exception {
		Vehicle vehicle = new Vehicle();
		vehicle.setOwnerId(1L);
		vehicle.setPlate("ABC123");
		vehicle.setParked(false);
		UserEntity user = new UserEntity();
		user.setEmail("owner@example.com");
		user.setConfidenceCircle(Collections.emptyList());

		when(vehicleService.findVehicleByPlate(anyString())).thenReturn(Optional.of(vehicle));
		when(userService.findUserById(anyLong())).thenReturn(Optional.of(user));

		mockMvc.perform(patch("/watchman/switch-state")
										.param("plate", "ABC123")
										.param("watchmanSelectedUser", "owner@example.com"))
						.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testKickParkedVehicle() throws Exception {
		Vehicle vehicle = new Vehicle();
		vehicle.setPlate("ABC123");

		when(vehicleService.findVehicleByPlate(anyString())).thenReturn(Optional.of(vehicle));

		mockMvc.perform(get("/watchman/kick")
										.param("plate", "ABC123"))
						.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testKickAllParkedVehicle() throws Exception {
		mockMvc.perform(get("/watchman/kick-all"))
						.andExpect(status().isOk());
	}
}
