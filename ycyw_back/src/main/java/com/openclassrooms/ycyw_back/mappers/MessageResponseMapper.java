package com.openclassrooms.ycyw_back.mappers;

import com.openclassrooms.ycyw_back.dtos.MessageResponse;
import com.openclassrooms.ycyw_back.entities.Message;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class MessageResponseMapper implements Function<Message, MessageResponse> {
    @Override
    public MessageResponse apply(Message message) {
        return new MessageResponse(message.getId(), message.getContent(), message.getCreatedAt(), message.getUser().getId(), message.getChat().getId());
    }
}
