package com.smartparkingupc.services;

import com.smartparkingupc.entities.Role;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.entities.UserRole;
import com.smartparkingupc.repositories.IUserRoleRepository;
import com.smartparkingupc.repositories.UserRepository;
import com.smartparkingupc.services.impl.RoleServiceImpl;
import com.smartparkingupc.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleServiceImpl roleService;

  @Mock
  private IUserRoleRepository userRoleRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveUser() {

    UserEntity userEntity = new UserEntity();
    userEntity.setEmail("test@example.com");
    userEntity.setPassword("password");

    Role defaultRole = new Role();
    defaultRole.setName("ROLE_USER");

    UserRole userRole = UserRole.builder()
            .user(userEntity)
            .role(defaultRole)
            .build();

    // Configurar mocks
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
    when(roleService.findDefaultRole()).thenReturn(defaultRole);
    when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

    // Llamar al método bajo prueba
    userService.saveUser(userEntity);

    // Verificar que los métodos del mock fueron llamados
    verify(userRepository, times(1)).save(userEntity);
    verify(roleService, times(1)).findDefaultRole();
    verify(userRoleRepository, times(1)).save(any(UserRole.class));
  }

  @Test
  void testLoadUserByEmail_UserExists() {
    // Datos de prueba
    String email = "test@example.com";
    UserEntity userEntity = new UserEntity();
    userEntity.setEmail(email);
    userEntity.setPassword("password");
    userEntity.setId(1L);

    Role role = new Role();
    role.setName("ROLE_USER");
    UserRole userRole = new UserRole();
    userRole.setUser(userEntity);
    userRole.setRole(role);
    Collection<UserRole> userRoles = new ArrayList<>();
    userRoles.add(userRole);

    // Configurar mocks
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
    when(userRoleRepository.findAllByUserId(userEntity.getId())).thenReturn((List<UserRole>) userRoles);

    // Llamar al método bajo prueba
    UserDetails userDetails = userService.loadUserByEmail(email);

    // Verificaciones
    assertNotNull(userDetails);
    assertEquals(userEntity.getEmail(), userDetails.getUsername());
    assertEquals(userEntity.getPassword(), userDetails.getPassword());
    assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

    // Verificar que los métodos del mock fueron llamados
    verify(userRepository, times(1)).findByEmail(email);
    verify(userRoleRepository, times(1)).findAllByUserId(userEntity.getId());
  }

  @Test
  void testFindByEmail_UserDoesNotExist() {
    // Datos de prueba
    String email = "notfound@example.com";

    // Configurar mocks
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // Llamar al método bajo prueba
    Optional<UserEntity> optUser = userService.findUserByEmail(email);

    // Verificacion
    assertEquals(optUser, Optional.empty());
  }
}
