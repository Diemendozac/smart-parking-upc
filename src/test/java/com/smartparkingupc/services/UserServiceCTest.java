package com.smartparkingupc.services;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceCTest {

  static final MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0.26")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @Autowired private IUserService userService;

  @Autowired private UserRepository userRepository;

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
    UserEntity user = new UserEntity();
    user.setEmail("test@example.com");
    user.setPassword("password");
    user.setName("test");
    userRepository.save(user);
  }

  @Test
  void testFindUserByEmail() {
    Optional<UserEntity> user = userService.findUserByEmail("test@example.com");
    assertTrue(user.isPresent());
    assertEquals("test@example.com", user.get().getEmail());
  }

  @Test
  void testSaveUser() {
    UserEntity newUser = new UserEntity();
    newUser.setEmail("newuser@example.com");
    newUser.setPassword("newpassword");
    userService.saveUser(newUser);

    Optional<UserEntity> savedUser = userRepository.findByEmail("newuser@example.com");
    assertTrue(savedUser.isPresent());
    assertEquals("newuser@example.com", savedUser.get().getEmail());
  }

  @Test
  void testFindUserByEmail_UserDoesNotExist() {
    String email = "notfound@example.com";

    // Llamar al método bajo prueba
    Optional<UserEntity> optUser = userService.findUserByEmail(email);

    // Verificacion
    assertEquals(optUser, Optional.empty());
  }
}
