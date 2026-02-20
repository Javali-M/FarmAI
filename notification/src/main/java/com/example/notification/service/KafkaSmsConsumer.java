package com.example.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.notification.dto.SmsRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaSmsConsumer {

    private TwilioClientService twilioClientService;

    @KafkaListener(topics = "sms-topic", groupId = "sms-group")
    public void consume(SmsRequest smsRequest) {

        twilioClientService.sendSms(smsRequest);
    }

}
