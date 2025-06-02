package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.ChatResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotifyInterface {
    void registerEmitter(int userId, SseEmitter emitter);

    void notifyChatUpdate(int userId, ChatResponse chatResponse);
}

