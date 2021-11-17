package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.BaseTest;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.domain.ChatRoom;
import com.norbertgal.couchsurfersbe.domain.Message;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.ChatRoomRepository;
import com.norbertgal.couchsurfersbe.repositories.MessageRepository;
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
public class MessageServiceTest extends BaseTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ChatRoomRepository chatRoomRepository;

    @MockBean
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        User user = setUpUser();
        User user2 = setUpUser();
        user2.setId(2L);

        user2.setFullName("xy");
        user2.setEmail("xy@asd.com");

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);
        chatRoom.getUsers().add(user);
        chatRoom.getUsers().add(user2);
        chatRoom.setName("New chat room");

        user.setChatRooms(Collections.singletonList(chatRoom));
        user2.setChatRooms(Collections.singletonList(chatRoom));

        Message message = setUpMessage();
        message.setUser(user);
        message.setChatRoom(chatRoom);

        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(Optional.of(user2)).when(userRepository).findUserByEmail("xy@asd.com");
        doReturn(Optional.of(chatRoom)).when(chatRoomRepository).findById(1L);
        doReturn(message).when(messageRepository).save(any());
    }

    @Test
    @DisplayName("Send message without exceptions")
    void sendMessage() {
        MessageRequestDTO messageRequestDTO = new MessageRequestDTO();
        messageRequestDTO.setContent("Content");
        messageRequestDTO.setSenderId(1L);

        try {
            MessageDTO message = messageService.sendMessage(messageRequestDTO, 1L);
            Assertions.assertEquals("Content", message.getContent());
            Assertions.assertEquals("Norbert Gál", message.getSenderName());
        } catch (NotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Send message with wrong chat room id")
    void sendMessageWithWrongChatRoomId() {
        MessageRequestDTO messageRequestDTO = new MessageRequestDTO();
        messageRequestDTO.setContent("Content");
        messageRequestDTO.setSenderId(1L);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            messageService.sendMessage(messageRequestDTO, 2L);
        });

        String expectedMessage = "Chat room is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Send message with wrong user id")
    void sendMessageWithWrongUserId() {
        MessageRequestDTO messageRequestDTO = new MessageRequestDTO();
        messageRequestDTO.setContent("Content");
        messageRequestDTO.setSenderId(3L);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            messageService.sendMessage(messageRequestDTO, 1L);
        });

        String expectedMessage = "User is not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
