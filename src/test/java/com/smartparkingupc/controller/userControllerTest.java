package com.smartparkingupc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.clearInvocations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartparkingupc.entities.UserEntity;
import com.smartparkingupc.services.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class userControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserService iuserService;
    private ObjectMapper objectMapper;

@Test

    void testGuardarUser(){
    // Given
    UserEntity user1 = UserEntity.builder()
            .email("acamilovanegas@unicersar.edu.co")
            .password("Anderson2023")
            .name("Anderson")
            .phoneNumber("3023057102")
            .photoUrl("http://annanananna")
            .build();
    given(iuserService.saveUser(any(UserEntity.class))).willAnswer((invocation)-> invocation.getArgument(0));


    }

}
