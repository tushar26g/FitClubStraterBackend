package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.entity.Member;

import java.util.List;

public interface MemberService {
    Member addMember(MemberRequestDto dto);
    List<Member> getMembersByOwner(Long gymOwnerId);
    List<Member> searchMembers(Long gymOwnerId, String search);

}

