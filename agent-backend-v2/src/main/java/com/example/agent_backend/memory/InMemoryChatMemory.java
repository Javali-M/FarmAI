package com.example.agent_backend.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

@Component
public class InMemoryChatMemory {

    private final ConcurrentHashMap<String, List<Message>> store = new ConcurrentHashMap<>();

    private static final int MAX_MESSAGES = 100;

    public void add(String sessionId, Message message) {
        store.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(message);

        List<Message> messages = store.get(sessionId);
        if (messages.size() > MAX_MESSAGES) {
            store.put(sessionId, new ArrayList<>(
                messages.subList(messages.size() - MAX_MESSAGES, messages.size())
            ));
        }
    }

    public List<Message> get(String sessionId) {
        return Collections.unmodifiableList(
            store.getOrDefault(sessionId, new ArrayList<>())
        );
    }

    public void clear(String sessionId) {
        store.remove(sessionId);
    }
}