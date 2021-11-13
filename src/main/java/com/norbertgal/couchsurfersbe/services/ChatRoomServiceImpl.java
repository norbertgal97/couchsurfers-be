package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.mapper.ChatRoomMapper;
import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ChatRoomRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.ErrorDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.EmptyFieldsException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.UnknownUserException;
import com.norbertgal.couchsurfersbe.domain.ChatRoom;
import com.norbertgal.couchsurfersbe.domain.Message;
import com.norbertgal.couchsurfersbe.domain.User;
import com.norbertgal.couchsurfersbe.repositories.ChatRoomRepository;
import com.norbertgal.couchsurfersbe.repositories.MessageRepository;
import com.norbertgal.couchsurfersbe.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomServiceImpl(UserRepository userRepository, ChatRoomRepository chatRoomRepository, MessageRepository messageRepository, ChatRoomMapper chatRoomMapper) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
        this.chatRoomMapper = chatRoomMapper;
    }

    @Override
    public ChatRoomDTO createChatRoom(ChatRoomRequestDTO request, Long userId) throws UnknownUserException, NotFoundException, EmptyFieldsException {
        if (request.getChatRoomName() == null || request.getChatRoomName().isEmpty() ||
                request.getRecipientEmail() == null || request.getRecipientEmail().isEmpty()) {
            throw new EmptyFieldsException(ErrorDTO.builder()
                    .timestamp(new Date())
                    .errorCode(422)
                    .errorMessage("Empty fields!")
                    .build());
        }

        Optional<User> optionalCreatorUser = userRepository.findById(userId);

        if (optionalCreatorUser.isEmpty())
            throw new UnknownUserException(ErrorDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        User creatorUser = optionalCreatorUser.get();

        Optional<User> optionalRecipientUser = userRepository.findUserByEmail(request.getRecipientEmail());

        if (optionalRecipientUser.isEmpty())
            throw new NotFoundException(ErrorDTO.builder()
                    .timestamp(new Date())
                    .errorCode(404)
                    .errorMessage(request.getRecipientEmail() + " is not found!")
                    .build());

        User recipientUser = optionalRecipientUser.get();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(request.getChatRoomName());
        chatRoom.getUsers().add(recipientUser);
        chatRoom.getUsers().add(creatorUser);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return chatRoomMapper.chatRoomToChatRoomDTO(savedChatRoom, userId);
    }

    @Override
    public List<ChatRoomDTO> getOwnChatRooms(Long userId) throws UnknownUserException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new UnknownUserException(ErrorDTO.builder().timestamp(new Date()).errorCode(500).errorMessage("User is not found!").build());

        User user = optionalUser.get();

        List<ChatRoom> chatRooms = user.getChatRooms();

        return chatRoomMapper.chatRoomListToChatRoomDTOList(chatRooms, userId);
    }
}
