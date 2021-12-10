package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.BaseTest;
import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.domain.ChatRoom;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.ChatRoomRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class ChatRoomServiceTest extends BaseTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        User user = setUpUser();
        User user2 = setUpUser();
        user2.setId(2L);

        user2.setFullName("xy");
        user2.setEmail("xy@asd.com");

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.getUsers().add(user);
        chatRoom.getUsers().add(user2);
        chatRoom.setName("New chat room");

        user.setChatRooms(Collections.singletonList(chatRoom));
        user2.setChatRooms(Collections.singletonList(chatRoom));

        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(Optional.of(user2)).when(userRepository).findUserByEmail("xy@asd.com");
        doReturn(chatRoom).when(chatRoomRepository).save(any());
    }

    @Test
    @DisplayName("Create a chat room without exceptions")
    void createChatRoom() {
        ChatRoomRequestDTO requestDTO = new ChatRoomRequestDTO();
        requestDTO.setChatRoomName("New chat room");
        requestDTO.setRecipientEmail("xy@asd.com");

        try {
            ChatRoomDTO chatRoomDTO = chatRoomService.createChatRoom(requestDTO, 1L);
            Assertions.assertEquals("New chat room", chatRoomDTO.getChatRoomName());
            Assertions.assertEquals("xy@asd.com", chatRoomDTO.getRecipientEmail());
        } catch (UnknownUserException | NotFoundException | EmptyFieldsException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Create a chat room with wrong user id")
    void createChatRoomWithWrongUserId() {
        ChatRoomRequestDTO requestDTO = new ChatRoomRequestDTO();
        requestDTO.setChatRoomName("New chat room");
        requestDTO.setRecipientEmail("xy@asd.com");

        Exception exception = assertThrows(UnknownUserException.class, () -> {
            chatRoomService.createChatRoom(requestDTO, 3L);
        });

        String expectedMessage = "User is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Create a chat room with invalid request")
    void createChatRoomWithInvalidRequest() {
        ChatRoomRequestDTO requestDTO = new ChatRoomRequestDTO();
        requestDTO.setRecipientEmail("xy@asd.com");

        Exception exception = assertThrows(EmptyFieldsException.class, () -> {
            chatRoomService.createChatRoom(requestDTO, 1L);
        });

        String expectedMessage = "Empty fields!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Get chat rooms without exceptions")
    void getOwnChatRooms() {
        try {
            List<ChatRoomDTO> chatRoomDTOs = chatRoomService.getOwnChatRooms(1L);
            Assertions.assertEquals(1, chatRoomDTOs.size());
        } catch (UnknownUserException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Get chat rooms with wrong user id")
    void getOwnChatRoomsWithWrongUserId() {
        Exception exception = assertThrows(UnknownUserException.class, () -> {
            chatRoomService.getOwnChatRooms(3L);
        });

        String expectedMessage = "User is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
