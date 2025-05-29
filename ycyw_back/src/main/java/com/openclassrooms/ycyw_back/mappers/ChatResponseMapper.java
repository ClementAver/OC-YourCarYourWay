package com.openclassrooms.ycyw_back.mappers;

import com.openclassrooms.ycyw_back.dtos.ChatResponse;
import com.openclassrooms.ycyw_back.entities.Chat;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ChatResponseMapper implements Function<Chat, ChatResponse> {
    @Override
    public ChatResponse apply(Chat chat) {
        return new ChatResponse(chat.getId(), chat.getCreationTime(), chat.getUpdateTime(), chat.getCustomer().getId(), chat.getEmployee().getId());
    }
}
