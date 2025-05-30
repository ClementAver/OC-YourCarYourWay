package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.MessageRequest;
import com.openclassrooms.ycyw_back.dtos.MessageResponse;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;

import java.util.stream.Stream;

public interface MessageInterface {
    MessageResponse createMessage(MessageRequest messageRequest) throws NotFoundException;

    Stream<MessageResponse> getMessagesByChatId(Integer id) throws NotFoundException;
}
