package com.example.agent_backend.tool;

import com.example.agent_backend.email.EmailProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailTool {

    private final EmailProducer emailProducer;

    public Mono<String> sendEmail(String to, String message) {
        return Mono.fromRunnable(() -> emailProducer.sendEmailRequest(to, message))
                .thenReturn("Email request queued successfully for: " + to)
                .onErrorResume(e -> Mono.just("Failed to queue email: " + e.getMessage()));
    }
}