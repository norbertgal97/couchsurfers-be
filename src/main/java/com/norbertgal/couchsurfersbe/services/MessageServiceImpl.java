package com.norbertgal.couchsurfersbe.services;

import com.norbertgal.couchsurfersbe.api.v1.model.ErrorDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.MessageRequestDTO;
import com.norbertgal.couchsurfersbe.api.v1.model.exception.NotFoundException;
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
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(UserRepository userRepository, ChatRoomRepository chatRoomRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageDTO sendMessage(MessageRequestDTO messageRequestDTO, Long chatRoomId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(messageRequestDTO.getSenderId());
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("User is not found!").build());
        }

        if (optionalChatRoom.isEmpty()) {
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Chat room is not found!").build());
        }

        User sender = optionalUser.get();
        ChatRoom chatRoom = optionalChatRoom.get();

        Message message = new Message();
        message.setUser(sender);
        message.setChatRoom(chatRoom);
        message.setContent(messageRequestDTO.getContent());

        Message savedMessage = messageRepository.save(message);

        return new MessageDTO(savedMessage.getId(), savedMessage.getUser().getId(), savedMessage.getUser().getFullName(), savedMessage.getContent(), message.getCreatedAt());
    }

    @Override
    public List<MessageDTO> getMessagesForChatRoom(Long chatRoomId) throws NotFoundException {
        List<Message> messages = messageRepository.findAllByChatRoomId(chatRoomId);

        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);

        if (chatRoom.isEmpty()) {
            throw new NotFoundException(ErrorDTO.builder().timestamp(new Date()).errorCode(404).errorMessage("Chat room is not found!").build());
        }

        return messages.stream()
                .map(message -> new MessageDTO(message.getId(), message.getUser().getId(),message.getUser().getFullName(),message.getContent(), message.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
