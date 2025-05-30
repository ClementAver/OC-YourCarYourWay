package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.MessageRequest;
import com.openclassrooms.ycyw_back.dtos.MessageResponse;
import com.openclassrooms.ycyw_back.entities.Chat;
import com.openclassrooms.ycyw_back.entities.Message;
import com.openclassrooms.ycyw_back.entities.User;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.mappers.MessageResponseMapper;
import com.openclassrooms.ycyw_back.repositories.MessageRepository;
import com.openclassrooms.ycyw_back.repositories.ChatRepository;
import com.openclassrooms.ycyw_back.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class MessageService implements MessageInterface {

private final MessageRepository messageRepository;
private final ChatRepository chatRepository;
private final UserRepository userRepository;
private final MessageResponseMapper messageResponseMapper;


    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository, MessageResponseMapper messageResponseMapper) {
        this.messageRepository = messageRepository;

        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageResponseMapper = messageResponseMapper;
    }

    /**
     * Get all messages for a given chat
     * @param id The chat identifier
     * @return All messages for this chat
     * @throws NotFoundException If the chat is not found
     */
    @Override
    public Stream<MessageResponse> getMessagesByChatId(Integer id) throws NotFoundException {
        chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation non référencé."));

        return messageRepository.findByChatId(id).stream().map(messageResponseMapper);
    }

    /**
     * Create a new comment
     * @param messageRequest The comment to create
     * @return The created comment
     * @throws NotFoundException If the post is not found
     */
    @Override
    public MessageResponse createMessage(MessageRequest messageRequest) throws NotFoundException {
        User user = userRepository.findById(messageRequest.getUser())
                .orElseThrow(() -> new NotFoundException("Utilisateur non référencé."));
        Chat chat = chatRepository.findById(messageRequest.getChat())
                .orElseThrow(() -> new NotFoundException("Conversation non référencé."));

        Message comment = new Message();
        comment.setUser(user);
        comment.setChat(chat);
        comment.setContent(messageRequest.getContent());

        messageRepository.save(comment);

        return messageResponseMapper.apply(comment);
    }
}
