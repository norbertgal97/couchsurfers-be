package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.BaseTest;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.SignUpRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.UserDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.AlreadyRegisteredEmailException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.domain.ChatRoom;
import com.norbertgal.couchsurfersbe.domain.Message;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = setUpUser();

        doReturn(user).when(userRepository).save(any());
    }

    @Test
    @DisplayName("Register user without exceptions")
    void register() {
        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setEmail("norbert.gal@asd.com");
        signUpRequestDTO.setFullName("Norbert Gál");
        signUpRequestDTO.setPassword("123456");

        try {
            UserDTO userDTO = userService.register(signUpRequestDTO);
            Assertions.assertEquals("norbert.gal@asd.com", userDTO.getEmail());
            Assertions.assertEquals("Norbert Gál", userDTO.getFullName());
            Assertions.assertEquals(1, userDTO.getId());
        } catch (AlreadyRegisteredEmailException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Register user with already registered email")
    void registerWithAlreadyRegisteredEmail() {
        User user = setUpUser();
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail("norbert.gal@asd.com");

        SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setEmail("norbert.gal@asd.com");
        signUpRequestDTO.setFullName("Norbert Gál");
        signUpRequestDTO.setPassword("123456");

        Exception exception = assertThrows(AlreadyRegisteredEmailException.class, () -> {
            userService.register(signUpRequestDTO);
        });

        String expectedMessage = "You have already registered with this email address!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
