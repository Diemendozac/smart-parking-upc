package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.UserEntity;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

  static final MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0.26")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

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
    UserEntity userEntity = new UserEntity();
    userEntity.setEmail("test@example.com");
    userEntity.setPassword("password");
    userRepository.save(userEntity);
  }

  @Test
  void testFindByEmail_UserExists() {
    Optional<UserEntity> foundUser = userRepository.findByEmail("test@example.com");
    assertTrue(foundUser.isPresent());
    assertEquals("test@example.com", foundUser.get().getEmail());
  }

  @Test
  void testFindByEmail_UserDoesNotExist() {
    Optional<UserEntity> foundUser = userRepository.findByEmail("notfound@example.com");
    assertFalse(foundUser.isPresent());
  }
}
