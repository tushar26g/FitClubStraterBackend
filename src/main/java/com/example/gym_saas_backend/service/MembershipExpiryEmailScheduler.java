package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.repository.MemberRepository;
import com.example.gym_saas_backend.repository.OwnerRepository;
import com.example.gym_saas_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MembershipExpiryEmailScheduler {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 5 * * ?") // every day at 9 AM
    public void sendMembershipExpiryEmails() {
        LocalDate today = LocalDate.now();
        List<LocalDate> targetDates = List.of(today.minusDays(2), today, today.plusDays(2));

        List<Member> expiringMembers = memberRepository.findExpiringMembers(targetDates);
        Map<Long, List<Member>> groupedByOwner = expiringMembers.stream()
                .filter(member -> member.getEmail() != null && !member.getEmail().isEmpty())
                .collect(Collectors.groupingBy(Member::getGymOwnerId));

        for (Map.Entry<Long, List<Member>> entry : groupedByOwner.entrySet()) {
            Long ownerId = entry.getKey();
            List<Member> members = entry.getValue();

            Optional<Owner> ownerOpt = ownerRepository.findById(ownerId);
            if (ownerOpt.isEmpty()) continue;

            Owner owner = ownerOpt.get();
            String ownerEmail = owner.getEmail();
            String ownerName = owner.getFullName();

            // Send owner summary
//            StringBuilder summary = new StringBuilder();
//            summary.append("Hi ").append(ownerName).append(",\n\n");
//            summary.append("Here is a list of members whose memberships are expiring:\n\n");
//            for (Member m : members) {
//                summary.append("- ").append(m.getName())
//                        .append(" | End Date: ").append(m.getMembershipEndDate()).append("\n");
//            }
//            summary.append("\nPlease follow up accordingly.\n\nThanks,\nYour Gym Management System");
            StringBuilder html = new StringBuilder();
            html.append("<h3>Expiring Memberships</h3>");
            html.append("<table border='1' cellpadding='5' cellspacing='0'>");
            html.append("<tr><th>Member Name</th><th>End Date</th></tr>");
            for (Member m : members) {
                html.append("<tr>")
                        .append("<td>").append(m.getName()).append("</td>")
                        .append("<td>").append(m.getMembershipEndDate()).append("</td>")
                        .append("</tr>");
            }
            html.append("</table>");

            emailService.sendHTMLEmail(ownerEmail, "[Reminder] Expiring Memberships", html.toString());

            // Send individual member emails
            for (Member m : members) {
                String body = String.format(
                        "Hi %s,\n\nYour gym membership is ending on %s. Please renew to avoid interruption.\n\nRegards,\n%s",
                        m.getName(), m.getMembershipEndDate(), owner.getBusinessName()
                );
                emailService.sendEmail(m.getEmail(), "Membership Expiry Reminder", body);
            }
        }
    }
}

