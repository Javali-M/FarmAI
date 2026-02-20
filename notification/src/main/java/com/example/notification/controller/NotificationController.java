package com.example.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification.service.KafkaProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.notification.dto.EmailRequest;
import com.example.notification.dto.SmsRequest;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        kafkaProducer.sendEmail(emailRequest);
        return ResponseEntity.accepted().body("email sent!!");
    }

    @PostMapping("/sms")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequest smsRequest) {
        kafkaProducer.sendSms(smsRequest);
        return ResponseEntity.accepted().body("sms sent!!");
    }

}
