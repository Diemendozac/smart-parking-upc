package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.Vehicle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class VehicleRepositoryTest {

  static final MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0.26")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @Autowired private VehicleRepository vehicleRepository;

  @BeforeAll
  static void startContainer() {
    mysqlContainer.start();
  }

  @DynamicPropertySource
  static void configureTestDatabase(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
    registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
  }

  @BeforeEach
  void setUp() {
    Vehicle vehicle = new Vehicle();
    vehicle.setOwnerId(1L);
    vehicle.setPlate("ABC123");
    vehicle.setParked(true);
    vehicleRepository.save(vehicle);
  }

  @Test
  void testFindAllByOwnerId() {
    List<Vehicle> vehicles = vehicleRepository.findAllByOwnerId(1L);
    assertFalse(vehicles.isEmpty());
    assertEquals(1L, vehicles.get(0).getOwnerId());
  }

  @Test
  void testFindByPlate() {
    Optional<Vehicle> vehicle = vehicleRepository.findByPlate("ABC123");
    assertTrue(vehicle.isPresent());
    assertEquals("ABC123", vehicle.get().getPlate());
  }

  @Test
  void testFindAllParkedVehicles() {
    List<Vehicle> parkedVehicles = vehicleRepository.findAllParkedVehicles();
    assertFalse(parkedVehicles.isEmpty());
    assertTrue(parkedVehicles.get(0).isParked());
  }

  @Test
  void testDeleteVehicleByPlate() {
    vehicleRepository.deleteVehicleByPlate("ABC123");
    Optional<Vehicle> vehicle = vehicleRepository.findByPlate("ABC123");
    assertFalse(vehicle.isPresent());
  }
}
