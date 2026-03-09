package com.example.agent_backend.email;

public class EmailRequest {

    private String to;
    private String message;

    public EmailRequest() {}

    public EmailRequest(String to, String message) {
        this.to = to;
        this.message = message;
    }

    public String getTo() { return to; }
    public String getMessage() { return message; }
    public void setTo(String to) { this.to = to; }
    public void setMessage(String message) { this.message = message; }
}