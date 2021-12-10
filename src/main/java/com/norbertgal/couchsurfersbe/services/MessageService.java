package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.MessageDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(MessageRequestDTO messageRequestDTO, Long chatRoomId) throws NotFoundException;
    List<MessageDTO> getMessagesForChatRoom(Long chatRoomId) throws NotFoundException;
}
