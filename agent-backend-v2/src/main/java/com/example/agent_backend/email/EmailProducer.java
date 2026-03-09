package com.example.agent_backend.email;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducer {

    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    private static final String TOPIC = "email-requests";

    public void sendEmailRequest(String to, String message) {
        EmailRequest request = new EmailRequest(to, message);
        kafkaTemplate.send(TOPIC, request);
        System.out.println("Email request queued to Kafka topic: " + TOPIC);
    }
}