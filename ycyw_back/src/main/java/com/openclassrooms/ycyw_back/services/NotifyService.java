package com.openclassrooms.ycyw_back.services;

import com.openclassrooms.ycyw_back.dtos.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotifyService implements NotifyInterface {

    private final Map<Integer, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    /**
     * Register an SSE emitter for a user
     * @param userId The user identifier
     * @param emitter The SSE emitter to register
     */
    @Override
    public void registerEmitter(int userId, SseEmitter emitter) {
        emitters.computeIfAbsent(userId, k -> new java.util.concurrent.CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> emitters.get(userId).remove(emitter));
        emitter.onTimeout(() -> emitters.get(userId).remove(emitter));
    }

    /**
     * Notify a user about a chat creation or update using SSE
     * @param userId The user identifier
     * @param chatResponse The chat
     */
    @Override
    public void notifyChatUpdate(int userId, ChatResponse chatResponse) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(chatResponse);
                } catch (Exception e) {
                    emitter.complete();
                }
            }
        }
    }
}
