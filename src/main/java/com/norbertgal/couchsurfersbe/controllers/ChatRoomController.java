package com.norbertgal.couchsurfersbe.controllers;

import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.services.ChatRoomService;
import com.norbertgal.couchsurfersbe.services.authentication.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ChatRoomController.BASE_URL)
public class ChatRoomController {
    public static final String BASE_URL = "/api/v1/chat-rooms";

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<ChatRoomDTO> createChatRoom(@RequestBody ChatRoomRequestDTO request,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) throws UnknownUserException, NotFoundException, EmptyFieldsException {
        return new ResponseEntity<>(chatRoomService.createChatRoom(request, userDetails.getUserId()), HttpStatus.CREATED);
    }

    @GetMapping("/own")
    public ResponseEntity<List<ChatRoomDTO>> getOwnChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) throws UnknownUserException {
        return new ResponseEntity<>(chatRoomService.getOwnChatRooms(userDetails.getUserId()), HttpStatus.OK);
    }


}
