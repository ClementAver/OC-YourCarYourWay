package com.openclassrooms.ycyw_back.controllers;

import com.openclassrooms.ycyw_back.dtos.MessageRequest;
import com.openclassrooms.ycyw_back.dtos.MessageResponse;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.services.JwtService;
import com.openclassrooms.ycyw_back.services.MessageService;
import com.openclassrooms.ycyw_back.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final JwtService jwtService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService, JwtService jwtService) {
        this.messageService = messageService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Get all messages for a given chat
     * @param id The chat identifier
     * @return All messages for this chat
     * @throws NotFoundException If the chat is not found
     */
    @GetMapping("/message/chat/{id}")
    public Stream<MessageResponse> getMessagesByChatId(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
       return messageService.getMessagesByChatId(id);
    }

    /**
     * Create a new message
     * @param messageRequest The message to create
     * @return The created message
     * @throws NotFoundException If the chat is not found
     */
    // Legacy : the App now use a WebSocket.
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/message")
    public MessageResponse createMessage(@Valid @RequestBody MessageRequest messageRequest) throws NotFoundException {
        return messageService.createMessage(messageRequest);
    }

    /**
     * Create a new message then notify chat subscribers using WebSocket
     * @param messageRequest The message to send
     * @return The created message
     * @throws NotFoundException If the chat is not found
     */
    @MessageMapping("/chat.sendMessage.{chatId}")
    // Indicates that the return value of a message-handling method should be sent as a Message to the specified destinations.
    @SendTo("/topic/chat.{chatId}")
    public MessageResponse sendMessage(MessageRequest messageRequest) throws NotFoundException {
        UserDetails userDetails = userService.getRawUser(messageRequest.getUser());
        /*
         * Invalid because : WS != HTTP.
         * TODO : custom messages for error handling.
         */
        if (!jwtService.isTokenValid(messageRequest.getToken(), userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }
        return messageService.createMessage(messageRequest);
    }
}
