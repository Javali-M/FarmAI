package com.example.notification.service;

import org.springframework.stereotype.Service;

import com.example.notification.dto.SmsRequest;
import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioClientService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(SmsRequest smsRequest) {
        try {

            Message message = Message.creator(
                    new PhoneNumber(smsRequest.getNumber()),
                    new PhoneNumber(fromNumber),
                    smsRequest.getContent())
                    .create();
            System.out.println("SMS sent with id: " + message.getSid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
