package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.TwilioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

    @Autowired
    private TwilioConfig twilioConfig;

    public String sendSms(String toPhoneNumber, String message) {
        try {
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioConfig.getTrialNumber()),
                    message
            ).create();
            return "SMS sent to " + toPhoneNumber;
        } catch (Exception e) {
            return "Failed to send SMS: " + e.getMessage();
        }
    }

}

