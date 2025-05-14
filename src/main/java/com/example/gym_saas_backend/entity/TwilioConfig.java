package com.example.gym_saas_backend.entity;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "twilio")
public class TwilioConfig {
    private String accountSid;
    private String authToken;
    private String trialNumber; // Your Twilio number

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // Getters and setters

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getTrialNumber() {
        return trialNumber;
    }

    public void setTrialNumber(String trialNumber) {
        this.trialNumber = trialNumber;
    }
}

