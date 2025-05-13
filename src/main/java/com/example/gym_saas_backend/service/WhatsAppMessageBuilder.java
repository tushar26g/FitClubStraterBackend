package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.Member;

import java.util.List;

public class WhatsAppMessageBuilder {

    public static String buildSummaryMessage(Long ownerId, List<Member> today, List<Member> in2Days, List<Member> twoDaysAgo) {
        StringBuilder msg = new StringBuilder("🔔 *Membership Expiry Summary*\n");

        if (!twoDaysAgo.isEmpty()) {
            msg.append("\n*⏰ Expired 2 Days Ago:*\n");
            twoDaysAgo.forEach(m -> msg.append("• ").append(m.getName()).append(" (").append(m.getMobileNumber()).append(")\n"));
        }

        if (!today.isEmpty()) {
            msg.append("\n*📅 Expiring Today:*\n");
            today.forEach(m -> msg.append("• ").append(m.getName()).append(" (").append(m.getMobileNumber()).append(")\n"));
        }

        if (!in2Days.isEmpty()) {
            msg.append("\n*⏳ Expiring in 2 Days:*\n");
            in2Days.forEach(m -> msg.append("• ").append(m.getName()).append(" (").append(m.getMobileNumber()).append(")\n"));
        }

        return msg.toString();
    }
}
