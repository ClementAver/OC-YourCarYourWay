package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.ChatRequest;
import com.openclassrooms.ycyw_back.dtos.ChatResponse;
import com.openclassrooms.ycyw_back.entities.Chat;
import com.openclassrooms.ycyw_back.entities.User;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.mappers.ChatResponseMapper;
import com.openclassrooms.ycyw_back.repositories.ChatRepository;
import com.openclassrooms.ycyw_back.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService implements ChatInterface {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatResponseMapper chatResponseMapper;

    @Autowired
    public ChatService(ChatRepository chatRepository, UserRepository userRepository, ChatResponseMapper chatResponseMapper, EntityManager entityManager) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatResponseMapper = chatResponseMapper;
    }

    /**
     * Create a new chat
     * @param chatRequest The chat to create
     * @return The created chat
     * @throws NotFoundException If the chat is not found
     */
    @Override
    public ChatResponse createChat(ChatRequest chatRequest) throws NotFoundException {
        User user = userRepository.findById(chatRequest.getUser())
                .orElseThrow(() -> new NotFoundException("Utilisateur non référencé."));

        User employee = userRepository.findById(1)
                .orElseThrow(() -> new NotFoundException("Employé non référencé."));

        Chat chat = new Chat();
        chat.setCustomer(user);
        chat.setEmployee(employee);


        chatRepository.save(chat);
        return chatResponseMapper.apply(chat);
    }

    /**
     * Get a chat by its identifier
     * @param id The chat identifier
     * @return The chat
     * @throws NotFoundException If the chat is not found
     */
    @Override
    public ChatResponse getChat(Integer id) throws NotFoundException {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation non référencé."));

        return chatResponseMapper.apply(chat);
    }
}
