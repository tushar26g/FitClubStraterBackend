package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Slf4j
public class SchedulerService {
    @Autowired
    private MemberRepository memberRepository;

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    // Runs every day at 2:00 AM
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void suspendExpiredMembers() {
        LocalDate today = LocalDate.now();

        List<Member> expiredMembers = memberRepository.findByMembershipStatusAndMembershipEndDateBefore(
                Member.MembershipStatus.ACTIVE, today
        );

        for (Member member : expiredMembers) {
            member.setMembershipStatus(Member.MembershipStatus.SUSPENDED);
        }

        memberRepository.saveAll(expiredMembers);

        log.info("Suspended {} expired members", expiredMembers.size());
    }

//    @Scheduled(cron = "0 0 10 * * *") // Every day at 10 AM
//    public void notifyMembershipStatus() {
//        List<Member> activeMembers = memberRepository.findByMembershipStatus(Member.MembershipStatus.ACTIVE);
//        Map<Long, List<Member>> membersGrouped = activeMembers.stream()
//                .collect(Collectors.groupingBy(member -> member.getGymOwner().getId()));
//
//        LocalDate today = LocalDate.now();
//
//        for (Map.Entry<Long, List<Member>> entry : membersGrouped.entrySet()) {
//            Long ownerId = entry.getKey();
//            List<Member> members = entry.getValue();
//
//            List<Member> endingToday = new ArrayList<>();
//            List<Member> endingIn2Days = new ArrayList<>();
//            List<Member> ended2DaysAgo = new ArrayList<>();
//
//            for (Member m : members) {
//                if (m.getMembershipEndDate() == null) continue;
//
//                long daysBetween = ChronoUnit.DAYS.between(today, m.getMembershipEndDate());
//                if (daysBetween == 0) endingToday.add(m);
//                else if (daysBetween == 2) endingIn2Days.add(m);
//                else if (daysBetween == -2) ended2DaysAgo.add(m);
//            }
//
//            String messageBody = WhatsAppMessageBuilder.buildSummaryMessage(
//                    ownerId, endingToday, endingIn2Days, ended2DaysAgo
//            );
//
//            String ownerPhone = gymOwnerRepository.findById(ownerId).get().getMobileNumber();
//            this.sendWhatsAppMessage(ownerPhone, messageBody);
//        }
//    }
//
//    public void sendWhatsAppMessage(String phoneNumber, String message) {
//        String apiUrl = "https://api.gupshup.io/sm/api/v1/msg";
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("apikey", "YOUR_GUPSHUP_API_KEY");
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("channel", "whatsapp");
//        params.add("source", "YOUR_REGISTERED_WHATSAPP_NUMBER");
//        params.add("destination", "91" + phoneNumber); // add country code
//        params.add("message", message);
//        params.add("src.name", "YOUR_GUPSHUP_APP_NAME");
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//        restTemplate.postForEntity(apiUrl, request, String.class);
//    }

}
