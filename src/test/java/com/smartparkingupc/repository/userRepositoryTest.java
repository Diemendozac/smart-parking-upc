package com.smartparkingupc.repository;

import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.http.request.UserCredential;
import com.smartparkingupc.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class userRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private UserEntity user;

    @BeforeEach
    void setup() {
        user = UserEntity.builder()
                .email("acvanegas@unicesar.edu.co")
                .password("Ander2023")
                .name("Camilo")
                .phoneNumber("3023057100")
                .photoUrl("http://jahsjhdasda")
                .id(200L)
                .build();
    }

    @DisplayName("test guardar usuarios")
    @Test
    void testSaveUser() {
        //given -- dado o condicion previa o configuracion
        UserEntity user1 = UserEntity.builder()
                .email("acamilovanegas@unicersar.edu.co")
                .password("Anderson2023")
                .name("Anderson")
                .phoneNumber("3023057102")
                .photoUrl("http://annanananna")
                .build();
        // when -- accion o el comportamiento
        UserEntity userSaved = userRepository.save(user1);

        // then -- verificar la salida
        assertThat(userSaved).isNotNull();
        assertThat(userSaved.getId()).isGreaterThan(0);

    }

    @DisplayName("test lista de usuarios")
    @Test
    void testListUsers() {
        //given -- dado o condicion previa o configuracion
        UserEntity user1 = UserEntity.builder()
                .email("acamilovanegas@unicersar.edu.co")
                .password("Anderson2023")
                .name("Anderson")
                .phoneNumber("3023057102")
                .photoUrl("http://annanananna")
                .build();
        userRepository.save(user1);
        userRepository.save(user);

        // when -- accion o el comportamiento
        List<UserEntity> ListaDeUsers = (List<UserEntity>) userRepository.findAll();

        // then -- verificar la salida
        assertThat(ListaDeUsers).isNotNull();
        assertThat(ListaDeUsers.size()).isGreaterThan(2);


    }

    @DisplayName("test para encontrar usuarios")
    @Test
    void testfindUserByEmail() {
        //When
        Optional<UserCredential> user2 = userRepository.findUserEntityByEmail("acvanegas@unicersar.edu.co");
        // UserEntity user2 = userRepository.findByEmail(user.getEmail()).get();

        //Then
        assertThat(user2).isNotNull();

    }

    @DisplayName("test para encontrar user by id")
    @Test
    void testfindUserByid() {
        UserEntity user4 = null;
        userRepository.save(user);
        //When
        Optional<UserEntity> user3 = userRepository.findById(user.getId());
        if(user3.isPresent())  user4 = user3.get();

        //then
        assertThat(user4).isNotNull();


    }

}
