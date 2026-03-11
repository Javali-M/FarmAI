package com.example.agent_backend.email;

public class EmailRequest {

    private String to;
    private String message;
    private String subject;

    public EmailRequest() {
    }

    public EmailRequest(String to, String message, String subject) {
        this.to = to;
        this.message = message;
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}