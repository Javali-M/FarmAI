package com.example.notification.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.notification.dto.EmailRequest;
import com.example.notification.dto.SmsRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmail(EmailRequest emailRequest) {
        kafkaTemplate.send("email-topic", emailRequest);
    }

    public void sendSms(SmsRequest smsRequest) {
        kafkaTemplate.send("sms-topic", smsRequest);
    }
}
