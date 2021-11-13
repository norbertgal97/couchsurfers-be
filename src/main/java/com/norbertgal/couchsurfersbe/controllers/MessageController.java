package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.MessageDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(MessageController.BASE_URL)
public class MessageController {
    public static final String BASE_URL = "/api/v1/messages";

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/queue/{chatRoomId}")
    public MessageDTO sendMessage(@DestinationVariable Long chatRoomId, MessageRequestDTO message) throws NotFoundException {
        return messageService.sendMessage(message, chatRoomId);
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessagesForChatRoom(@RequestParam("chat-room-id") Long chatRoomId) throws NotFoundException {
        return new ResponseEntity<>(messageService.getMessagesForChatRoom(chatRoomId), HttpStatus.OK);
    }

}
