package com.smartparkingupc.repositories;

import com.smartparkingupc.entities.UserEntity;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository underTest;

  @PostConstruct
  public void initDatabase() {
    //jdbcTemplate.execute("init-db.sql");
    System.out.println("Ok");
  }
  static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
          .withCreateContainerCmdModifier(cmd -> cmd.withName("mysql_test"));
          //.withInitScript("init-db.sql");

  @DynamicPropertySource
  static void setMySQLContainer(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);

  }

  @BeforeAll
  static void beforeAll() {
    mySQLContainer.start();
  }

  @AfterAll
  static void afterAll() {
    mySQLContainer.stop();
  }

  @Test
  void itShouldFindByEmail() {
    List<UserEntity> users = (List<UserEntity>) underTest.findAll();
    System.out.println(users);
  }

  @Test
  void itShouldFindUserEntityByEmail() {
  }
}