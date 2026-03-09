package com.example.agent_backend.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final SesClient sesClient;

    @Value("${ses.from-email}")
    private String fromEmail;

    @KafkaListener(topics = "email-requests", groupId = "email-group")
    public void consume(EmailRequest request) {
        System.out.println("Consumed email request for: " + request.getTo());
        try {
            SendEmailRequest sesRequest = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(Destination.builder()
                            .toAddresses(request.getTo())
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data("Message from FarmAI Agent")
                                    .charset("UTF-8")
                                    .build())
                            .body(Body.builder()
                                    .text(Content.builder()
                                            .data(request.getMessage())
                                            .charset("UTF-8")
                                            .build())
                                    .build())
                            .build())
                    .build();

            SendEmailResponse response = sesClient.sendEmail(sesRequest);
            System.out.println("Email sent! Message ID: " + response.messageId());

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}