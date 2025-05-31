package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.ChatRequest;
import com.openclassrooms.ycyw_back.dtos.ChatResponse;
import com.openclassrooms.ycyw_back.entities.Chat;
import com.openclassrooms.ycyw_back.entities.User;
import com.openclassrooms.ycyw_back.enums.Role;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.mappers.ChatResponseMapper;
import com.openclassrooms.ycyw_back.repositories.ChatRepository;
import com.openclassrooms.ycyw_back.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ChatService implements ChatInterface {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatResponseMapper chatResponseMapper;

    @Autowired
    public ChatService(ChatRepository chatRepository, UserRepository userRepository, ChatResponseMapper chatResponseMapper) {
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

        User employee = userRepository.findByRole(Role.EMPLOYEE)
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

    /**
     * Get all chats for a given user
     * @param id The user identifier
     * @return All chats for this user
     * @throws NotFoundException If the user is not found
     */
    @Override
    public Stream<ChatResponse> getChatsByUserId(Integer id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utilisateur non référencé."));

        List<Chat> chats;

        if (user.getRole() == Role.EMPLOYEE) {
            chats = chatRepository.findByEmployeeId(user.getId());
        } else {
            chats = chatRepository.findByCustomerId(user.getId());
        }

        System.out.println("Nombre de chats : " + chats.size());

        return chats.stream().map(chatResponseMapper);
    }



    /**
     * Delete a chat by its identifier
     * @param id The chat identifier
     * @return The identifier of the deleted chats
     * @throws NotFoundException If the chat is not found
     */
    @Override
    public int deleteChat(Integer id) throws NotFoundException {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation non référencé."));

        chatRepository.delete(chat);
        return chat.getId();
    }
}
