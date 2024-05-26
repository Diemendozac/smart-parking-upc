package com.smartparkingupc.controllers;

import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.services.IVehicleService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VehicleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IVehicleService vehicleService;

	@MockBean
	private JWTTokenUtil jwtTokenUtil;

	private Vehicle vehicle;

	@BeforeEach
	public void setUp() {
		vehicle = Vehicle.builder()
						.plate("ABC123")
						.model(2020) // model as int
						.line("Line Y")
						.brand("Brand Z")
						.ownerId(1L)
						.isParked(false)
						.build();

		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setPlate("ABC123");
		vehicleDTO.setModel(2020); // model as int
		vehicleDTO.setLine("Line Y");
		vehicleDTO.setBrand("Brand Z");


	}

	@Test
	public void testGetVehicles() throws Exception {
		when(vehicleService.findAll()).thenReturn(new ArrayList<>());

		mockMvc.perform(get("/vehicle/all"))
						.andExpect(status().isOk());
	}

	@Test
	public void testFindVehicleByPlate_Success() throws Exception {
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.of(vehicle));

		mockMvc.perform(get("/vehicle/")
										.param("plate", "ABC123"))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.plate").value("ABC123"));
	}

	@Test
	public void testFindVehicleByPlate_NotFound() throws Exception {
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.empty());

		mockMvc.perform(get("/vehicle/")
										.param("plate", "ABC123"))
						.andExpect(status().isNoContent());
	}

	@Test
	public void testSaveVehicle_Success() throws Exception {
		when(vehicleService.findOwnerRequestIdByUserEmail("user@example.com")).thenReturn(1L);
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.empty());

		mockMvc.perform(post("/vehicle/save")
										.requestAttr("LoggedInUser", "user@example.com")
										.contentType(MediaType.APPLICATION_JSON)
										.content("{ \"plate\": \"ABC123\", \"model\": 2020, \"line\": \"Line Y\", \"brand\": \"Brand Z\" }"))
						.andExpect(status().isOk());
	}

	@Test
	public void testSaveVehicle_VehicleExists() throws Exception {
		when(vehicleService.findOwnerRequestIdByUserEmail("user@example.com")).thenReturn(1L);
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.of(vehicle));

		mockMvc.perform(post("/vehicle/save")
										.requestAttr("LoggedInUser", "user@example.com")
										.contentType(MediaType.APPLICATION_JSON)
										.content("{ \"plate\": \"ABC123\", \"model\": 2020, \"line\": \"Line Y\", \"brand\": \"Brand Z\" }"))
						.andExpect(status().isUnauthorized());
	}

	@Test
	public void testSaveVehicle_OwnerNotFound() throws Exception {
		when(vehicleService.findOwnerRequestIdByUserEmail("user@example.com")).thenReturn(-1L);

		mockMvc.perform(post("/vehicle/save")
										.requestAttr("LoggedInUser", "user@example.com")
										.contentType(MediaType.APPLICATION_JSON)
										.content("{ \"plate\": \"ABC123\", \"model\": 2020, \"line\": \"Line Y\", \"brand\": \"Brand Z\" }"))
						.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateVehicle_Success() throws Exception {
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.of(vehicle));
		when(vehicleService.findOwnerRequestIdByUserEmail("user@example.com")).thenReturn(1L);

		mockMvc.perform(put("/vehicle/update")
										.requestAttr("LoggedInUser", "user@example.com")
										.contentType(MediaType.APPLICATION_JSON)
										.content("{ \"plate\": \"ABC123\", \"model\": 2021, \"line\": \"Line Z\", \"brand\": \"Brand A\" }"))
						.andExpect(status().isOk());
	}

	@Test
	public void testUpdateVehicle_NotFound() throws Exception {
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.empty());

		mockMvc.perform(put("/vehicle/update")
										.requestAttr("LoggedInUser", "user@example.com")
										.contentType(MediaType.APPLICATION_JSON)
										.content("{ \"plate\": \"ABC123\", \"model\": 2021, \"line\": \"Line Z\", \"brand\": \"Brand A\" }"))
						.andExpect(status().isNoContent());
	}

	@Test
	public void testUpdateVehicle_Unauthorized() throws Exception {
		vehicle.setOwnerId(2L);
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.of(vehicle));
		when(vehicleService.findOwnerRequestIdByUserEmail("user@example.com")).thenReturn(1L);

		mockMvc.perform(put("/vehicle/update")
										.requestAttr("LoggedInUser", "user@example.com")
										.contentType(MediaType.APPLICATION_JSON)
										.content("{ \"plate\": \"ABC123\", \"model\": 2021, \"line\": \"Line Z\", \"brand\": \"Brand A\" }"))
						.andExpect(status().isUnauthorized());
	}

	@Test
	public void testDeleteVehicle_Success() throws Exception {
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.of(vehicle));
		when(vehicleService.findOwnerRequestIdByUserEmail("user@example.com")).thenReturn(1L);

		mockMvc.perform(delete("/vehicle/delete")
										.requestAttr("LoggedInUser", "user@example.com")
										.param("vehiclePlate", "ABC123"))
						.andExpect(status().isOk());
	}

	@Test
	public void testDeleteVehicle_NotFound() throws Exception {
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.empty());

		mockMvc.perform(delete("/vehicle/delete")
										.requestAttr("LoggedInUser", "user@example.com")
										.param("vehiclePlate", "ABC123"))
						.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteVehicle_Unauthorized() throws Exception {
		vehicle.setOwnerId(2L);
		when(vehicleService.findVehicleByPlate("ABC123")).thenReturn(Optional.of(vehicle));
		when(vehicleService.findOwnerRequestIdByUserEmail("user@example.com")).thenReturn(1L);

		mockMvc.perform(delete("/vehicle/delete")
										.requestAttr("LoggedInUser", "user@example.com")
										.param("vehiclePlate", "ABC123"))
						.andExpect(status().isUnauthorized());
	}
}
