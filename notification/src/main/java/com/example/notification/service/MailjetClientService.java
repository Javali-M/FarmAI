package com.example.notification.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.notification.dto.EmailRequest;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;

@Service
public class MailjetClientService {

        @Value("${mailjet.api-key}")
        private String apiKey;

        @Value("${mailjet.secret-key}")
        private String secretKey;

        public void sendEmail(EmailRequest emailRequest) {

                try {
                        ClientOptions options = ClientOptions.builder()
                                        .apiKey(apiKey)
                                        .apiSecretKey(secretKey)
                                        .build();
                        MailjetClient client = new MailjetClient(options);

                        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                                        .property(Emailv31.MESSAGES, new JSONArray()
                                                        .put(new JSONObject()
                                                                        .put(Emailv31.Message.FROM,
                                                                                        new JSONObject()
                                                                                                        .put("Email", "y.abhisekhmessi535@gmail.com")
                                                                                                        .put("Name", "FarmAI Org"))
                                                                        .put(Emailv31.Message.TO,
                                                                                        new JSONArray()
                                                                                                        .put(new JSONObject()
                                                                                                                        .put("Email", emailRequest
                                                                                                                                        .getEmail())))
                                                                        .put(Emailv31.Message.SUBJECT,
                                                                                        emailRequest.getSubject())
                                                                        .put(Emailv31.Message.TEXTPART,
                                                                                        emailRequest.getContent())));

                        MailjetResponse response = client.post(request);
                        System.out.println("Email sent with status: " + response.getStatus());
                        System.out.println("Email response: " + response.getData());
                } catch (Exception e) {

                        e.printStackTrace();
                }
        }
}
