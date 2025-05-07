package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
}
