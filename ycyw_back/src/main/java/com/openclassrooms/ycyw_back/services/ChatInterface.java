package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.ChatRequest;
import com.openclassrooms.ycyw_back.dtos.ChatResponse;
import com.openclassrooms.ycyw_back.exceptions.NotFoundException;

import java.util.stream.Stream;

public interface ChatInterface {
    ChatResponse createChat(ChatRequest chatRequest) throws NotFoundException;

    ChatResponse getChat(Integer id) throws NotFoundException;

    Stream<ChatResponse> getChatsByUserId (Integer id) throws NotFoundException;

    int deleteChat(Integer id) throws NotFoundException;
}