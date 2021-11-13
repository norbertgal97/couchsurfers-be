package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;

import java.util.List;

public interface ChatRoomService {
    ChatRoomDTO createChatRoom(ChatRoomRequestDTO request, Long userId) throws UnknownUserException, NotFoundException, EmptyFieldsException;
    List<ChatRoomDTO> getOwnChatRooms(Long userId) throws UnknownUserException;

}
