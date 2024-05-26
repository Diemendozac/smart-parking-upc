package com.smartparkingupc.services;

import com.smartparkingupc.controllers.dto.VehicleDTO;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.Vehicle;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.repositories.VehicleRepository;
import com.smartparkingupc.services.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceImplTest {

	@Mock
	private VehicleRepository vehicleRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private VehicleServiceImpl vehicleService;

	private Vehicle vehicle;
	private UserEntity user;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle();
		vehicle.setPlate("ZXC516");
		vehicle.setBrand("Kawazaki");
		vehicle.setModel(2022);
		vehicle.setLine("Melkov");
		vehicle.setOwnerId(1L);

		user = new UserEntity();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setConfidenceCircle(new ArrayList<>());
	}

	@Test
	void testFindAll() {
		when(vehicleRepository.findAll()).thenReturn(Collections.singletonList(vehicle));

		List<Vehicle> result = vehicleService.findAll();

		assertEquals(1, result.size());
		assertEquals("ZXC516", result.get(0).getPlate());
		verify(vehicleRepository, times(1)).findAll();
	}

	@Test
	void testFindVehicleByPlate() {
		when(vehicleRepository.findByPlate("ABC123")).thenReturn(Optional.of(vehicle));

		Optional<Vehicle> result = vehicleService.findVehicleByPlate("ABC123");

		assertTrue(result.isPresent());
		assertEquals("ZXC516", result.get().getPlate());
		verify(vehicleRepository, times(1)).findByPlate("ABC123");
	}

	@Test
	void testSave() {
		when(vehicleRepository.findAllByOwnerId(1L)).thenReturn(Collections.emptyList());

		vehicleService.save(vehicle, 1L);

		verify(vehicleRepository, times(1)).save(vehicle);
	}

	@Test
	void testSaveLimitExceeded() {
		when(vehicleRepository.findAllByOwnerId(1L)).thenReturn(Arrays.asList(vehicle, vehicle, vehicle));

		vehicleService.save(vehicle, 1L);

		verify(vehicleRepository, never()).save(vehicle);
	}

	@Test
	void testUpdateVehicle() {
		vehicleService.updateVehicle(vehicle);

		verify(vehicleRepository, times(1)).save(vehicle);
	}

	@Test
	void testDeleteByPlate() {
		vehicleService.deleteByPlate("ABC123");

		verify(vehicleRepository, times(1)).deleteVehicleByPlate("ABC123");
	}

	@Test
	void testFindOwnerRequestIdByUserEmail() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		Long result = vehicleService.findOwnerRequestIdByUserEmail("test@example.com");

		assertEquals(1L, result);
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void testFindOwnerRequestIdByUserEmailNotFound() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

		Long result = vehicleService.findOwnerRequestIdByUserEmail("test@example.com");

		assertEquals(-1L, result);
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void testFindAllParkedVehicles() {
		when(vehicleRepository.findAllParkedVehicles()).thenReturn(Collections.singletonList(vehicle));

		List<VehicleDTO> result = vehicleService.findAllParkedVehicles();

		assertEquals(1, result.size());
		assertEquals("ZXC516", result.get(0).getPlate());
		verify(vehicleRepository, times(1)).findAllParkedVehicles();
	}

	@Test
	void testFindAssociatedVehicles() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(vehicleRepository.findAllByOwnerId(1L)).thenReturn(Collections.singletonList(vehicle));

		List<VehicleDTO> result = vehicleService.findAssociatedVehicles(1L);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("ZXC516", result.get(0).getPlate());
		verify(userRepository, times(1)).findById(1L);
		verify(vehicleRepository, times(1)).findAllByOwnerId(1L);
	}

	@Test
	void testFindAssociatedVehiclesUserNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		List<VehicleDTO> result = vehicleService.findAssociatedVehicles(1L);

		assertNull(result);
		verify(userRepository, times(1)).findById(1L);
		verify(vehicleRepository, never()).findAllByOwnerId(anyLong());
	}
}
