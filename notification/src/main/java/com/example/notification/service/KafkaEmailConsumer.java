package com.example.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.notification.dto.EmailRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaEmailConsumer {

    private final MailjetClientService mailjetClientService;

    // public KafkaEmailConsumer(MailjetClientService mailjetClientService) {
    // this.mailjetClientService = mailjetClientService;
    // }

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void consume(EmailRequest emailRequest) {
        mailjetClientService.sendEmail(emailRequest);
    }
}
