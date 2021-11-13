package com.norbertgal.couchsurfersbe.api.v1.mapper;

import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomDTO;
import com.norbertgal.couchsurfersbe.domain.ChatRoom;
import com.norbertgal.couchsurfersbe.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    default ChatRoomDTO chatRoomToChatRoomDTO(ChatRoom chatRoom, Long userId) {
        ChatRoomDTO dto = new ChatRoomDTO();

        dto.setId(chatRoom.getId());
        dto.setMyId(userId);
        dto.setChatRoomName(chatRoom.getName());
        
        Optional<User> recipient = chatRoom.getUsers().stream().filter(user -> !user.getId().equals(userId)).findFirst();

        recipient.ifPresent(user -> dto.setRecipientEmail(user.getEmail()));

        return dto;
    }

    default List<ChatRoomDTO> chatRoomListToChatRoomDTOList(List<ChatRoom> chatRooms, Long userId) {
        List<ChatRoomDTO> newChatRooms = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            newChatRooms.add(chatRoomToChatRoomDTO(room, userId));
        }

        return newChatRooms;
    }
}
