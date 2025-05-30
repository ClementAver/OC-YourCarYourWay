package com.openclassrooms.ycyw_back.controllers;

import com.openclassrooms.ycyw_back.dtos.MessageRequest;
import com.openclassrooms.ycyw_back.dtos.MessageResponse;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;
import com.openclassrooms.ycyw_back.services.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
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
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/message")
    public MessageResponse createMessage(@Valid @RequestBody MessageRequest messageRequest) throws NotFoundException {
        return messageService.createMessage(messageRequest);
    }
}
