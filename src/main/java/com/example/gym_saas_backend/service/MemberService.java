package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.AnalysisDTO;
import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.entity.Owner;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MemberService {
    Member addMember(MemberRequestDto dto, MultipartFile profilePhoto);
    boolean deleteMemberByIdAndOwnerId(Long memberId, Long ownerId);
    void updateMembershipStatus(Long gymOwnerId, Long memberId, Member.MembershipStatus status);
    List<Member> getMembersByOwnerAndStatus(Long gymOwnerId, Member.MembershipStatus status);
    List<Member> searchMembersWithStatus(Long gymOwnerId, String search, Member.MembershipStatus status);
    Member updateMember(MemberRequestDto dto, MultipartFile profilePhoto);
    AnalysisDTO analysisMembers(Long gymOwnerId);
    Boolean sendExcelToEmail(MultipartFile file, String owner, String name, String mobileNumber) throws MessagingException, IOException;
}

