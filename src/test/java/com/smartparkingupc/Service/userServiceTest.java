package com.smartparkingupc.Service;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;


import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import com.smartparkingupc.services.UserServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//
public class userServiceTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserEntity user;

    // El contenedor se declara como un campo estático
    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("projectdb")
            .withUsername("root")
            .withPassword("Anderson2023");

    // Método para inicializar y configurar la conexión a la base de datos
    @BeforeAll
    static void setUp() {
        mySQLContainer.start();
    }

    // Método para detener el contenedor después de que se completen las pruebas
    @AfterAll
    static void tearDown() {
        mySQLContainer.stop();
    }
    @Sql(scripts = { "/import.sql" })
    @Test
    void testMySQLContainerIsRunning() {
        assertThat(mySQLContainer.isRunning()).isTrue();
    }

    @Test
    //@Sql("/import.sql") // Ejecuta el script import.sql antes de ejecutar esta prueba
    void testImportedData() {
        int userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        assertTrue(userCount > 0, "Se esperaba que al menos un usuario estuviera presente en la tabla 'users'");

        System.out.println(userCount);
    }
    @Test
    //@Sql("/import.sql") // Ejecuta el script import.sql antes de ejecutar esta prueba
    void testImportedData2() {
        String username = jdbcTemplate.queryForObject("SELECT name FROM users WHERE id = 1", String.class);
        assertTrue(username != null && !username.isEmpty(), "Se esperaba que al menos un usuario estuviera presente en la tabla 'users'");
        System.out.println(username);

    }
    // Aquí puedes escribir tus otras pruebas







/*
    @Test
    void testSaveUser(){



        UserEntity userSaved = userService.saveUser(user1);

        assertThat(userSaved).isNotNull();
        assertThat(userSaved.getId()).isGreaterThan(0);

    }
*/


@Test
    void testListUsers() {

        //given -- dado o condicion previa o configuracion



        // when -- accion o el comportamiento

        List<UserEntity> ListaDeUsers = userService.findAll();

        // then -- verificar la salida
        assertThat(ListaDeUsers).isNotNull();
        assertThat(ListaDeUsers.size()).isGreaterThan(2);


    }

/*
    @Test
    void testFindAll() {
        user = UserEntity.builder()
                .email("acvanegas@unicesar.edu.co")
                .password("Ander2023")
                .name("Camilo")
                .phoneNumber("3023057100")
                .photoUrl("http://jahsjhdasda")
                .id(200L)
                .build();


        List<UserEntity> ListaDeUsers = (List<UserEntity>) userRepository.findAll();


    }*/






    }



