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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ChatService implements ChatInterface {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatResponseMapper chatResponseMapper;
    private final NotifyService notifyService;

    @Autowired
    public ChatService(ChatRepository chatRepository, UserRepository userRepository, ChatResponseMapper chatResponseMapper, NotifyService notifyService) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatResponseMapper = chatResponseMapper;
        this.notifyService = notifyService;
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

        notifyService.notifyChatUpdate(chat.getEmployee().getId(), chatResponseMapper.apply(chat));

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

        return chats.stream().map(chatResponseMapper);
    }

    /**
     * Update a chat
     * @param id The chat identifier
     * @throws NotFoundException If the chat is not found
     */
    public void updateChat(int id, boolean pending) throws NotFoundException {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation non référencé."));

        chat.setUpdatedAt(LocalDateTime.now()); // Useless, to be checked.
        chat.setPending(pending);
        chatRepository.save(chat);
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
