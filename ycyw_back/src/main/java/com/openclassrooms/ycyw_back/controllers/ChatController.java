package com.openclassrooms.ycyw_back.controllers;

import com.openclassrooms.ycyw_back.dtos.ChatRequest;
import com.openclassrooms.ycyw_back.dtos.ChatResponse;
import com.openclassrooms.ycyw_back.services.ChatService;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.services.JwtService;
import com.openclassrooms.ycyw_back.services.NotifyService;
import com.openclassrooms.ycyw_back.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;
    private final JwtService jwtService;
    private final UserService userService;
    private final NotifyService notifyService;

    public ChatController(ChatService chatService, UserService userService, JwtService jwtService, NotifyService notifyService) {
        this.chatService = chatService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.notifyService = notifyService;
    }

    /**
     * Create a new chat
     * @param chatRequest The chat to create
     * @return The created chat
     * @throws NotFoundException If the chat is not found
     */
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/chat")
    public ChatResponse createChat(@Valid @RequestBody ChatRequest chatRequest) throws NotFoundException {
        return chatService.createChat(chatRequest);
    }

    /**
     * Get a chat by its identifier
     * @param id The chat identifier
     * @return The chat
     * @throws NotFoundException If the chat is not found
     */
    @GetMapping("/chat/{id}")
    public ChatResponse getChat(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
            return chatService.getChat(id);
    }

    /**
     * Get all chats for a given user
     * @param id The user identifier
     * @return All chats for this user
     * @throws NotFoundException If the user is not found
     */
    @GetMapping("/chat/user/{id}")
    public Stream<ChatResponse> getChatsByUserId(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
        return chatService.getChatsByUserId(id);
    }

    /**
     * Delete a chat
     * @param id The chat identifier
     * @return The identifier of the deleted chat
     * @throws NotFoundException If the chat is not found
     */
    @DeleteMapping("/chat/{id}")
    public int deleteChat(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
        return chatService.deleteChat(id);
    }

    /**
     * Keeps track of the chats for a given user using Server-Sent Events (SSE).
     * @param id The user identifier
     * @param token The JWT token
     * @return A Server-Sent Events (SSE) emitter for the chats
     * @throws NotFoundException If the user is not found
     */
    @GetMapping("/chat/user/{id}/stream")
    public SseEmitter streamChatsByUserId(
            @PathVariable int id,
            @RequestParam("token") String token
    ) throws NotFoundException {
        UserDetails userDetails = userService.getRawUser(id);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }
        SseEmitter emitter = new SseEmitter(10800000L); // Three hours
        notifyService.registerEmitter(id, emitter);
        return emitter;
    }
}
