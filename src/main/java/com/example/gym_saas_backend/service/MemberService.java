package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.entity.Member;

import java.util.List;

public interface MemberService {
    Member addMember(MemberRequestDto dto);
    boolean deleteMemberByIdAndOwnerId(Long memberId, Long ownerId);
    void updateMembershipStatus(Long gymOwnerId, Long memberId, Member.MembershipStatus status);
    List<Member> getMembersByOwnerAndStatus(Long gymOwnerId, Member.MembershipStatus status);
    List<Member> searchMembersWithStatus(Long gymOwnerId, String search, Member.MembershipStatus status);
    Member updateMember(MemberRequestDto dto);

}

