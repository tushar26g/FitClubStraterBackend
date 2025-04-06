package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.repository.MemberRepository;
import com.example.gym_saas_backend.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member addMember(MemberRequestDto dto) {
        // First check if member exists
        Optional<Member> existingMemberOpt = memberRepository
                .findByGymOwnerIdAndMobileNumberOrEmail(dto.getGymOwnerId(), dto.getMobileNumber(), dto.getEmail());

        if (existingMemberOpt.isPresent()) {
            Member existing = existingMemberOpt.get();
            if (existing.getMembershipStatus() == Member.MembershipStatus.SUSPENDED) {
                throw new IllegalStateException("This member is suspended in your gym.");
            } else {
                throw new IllegalStateException("This member is already added to your gym.");
            }
        }

        // Create new member
        Member member = new Member();
        member.setName(dto.getName());
        member.setJoiningDate(dto.getJoiningDate());
        member.setMembershipEndDate(dto.getMembershipEndDate());
        member.setPackageName(dto.getPackageName());

        if (dto.getPaymentStatus() != null)
            member.setPaymentStatus(Member.PaymentStatus.valueOf(dto.getPaymentStatus().toUpperCase()));

        member.setAmountPaid(dto.getAmountPaid());
        member.setMobileNumber(dto.getMobileNumber());

        if (dto.getEmail() != null)
            member.setEmail(dto.getEmail());

        if (dto.getMembershipStatus() != null)
            member.setMembershipStatus(Member.MembershipStatus.valueOf(dto.getMembershipStatus().toUpperCase()));

        if (dto.getProfilePhotoUrl() != null)
            member.setProfilePhotoUrl(dto.getProfilePhotoUrl());

        if (dto.getPaymentMethod() != null)
            member.setPaymentMethod(Member.PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase()));

        if (dto.getMembershipPhotoUrl() != null)
            member.setMembershipPhotoUrl(dto.getMembershipPhotoUrl());

        member.setGymOwnerId(dto.getGymOwnerId());

        return memberRepository.save(member);
    }


    @Override
    public List<Member> getMembersByOwner(Long gymOwnerId) {
        return memberRepository.findByGymOwnerIdOrderByMembershipEndDateAsc(gymOwnerId);
    }

    @Override
    public List<Member> searchMembers(Long gymOwnerId, String search) {
        return memberRepository.searchByGymOwnerIdAndKeyword(gymOwnerId, search);
    }

}
