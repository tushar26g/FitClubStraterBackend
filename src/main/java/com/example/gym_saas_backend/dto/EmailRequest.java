package com.example.gym_saas_backend.dto;

public class EmailRequest {
    private String to;
    private String subject;
    private String body;
    private boolean isHtml;

    public EmailRequest(String to, String subject, String body, boolean isHtml) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.isHtml = isHtml;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public boolean isHtml() {
        return isHtml;
    }
}
