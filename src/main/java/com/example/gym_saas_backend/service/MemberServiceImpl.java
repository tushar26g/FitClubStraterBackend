package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.AnalysisDTO;
import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.other.ImageCompressor;
import com.example.gym_saas_backend.repository.MemberRepository;
import com.example.gym_saas_backend.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member addMember(MemberRequestDto dto, MultipartFile profilePhoto) {
        // First check if member exists
        Optional<Member> existingMemberOpt = memberRepository
                .findByGymOwnerIdAndMobileNumber(dto.getGymOwnerId(), dto.getMobileNumber());

        if (existingMemberOpt.isPresent()) {
            Member existing = existingMemberOpt.get();
            if (existing.getMembershipStatus() == Member.MembershipStatus.SUSPENDED) {
                throw new IllegalStateException("This member is suspended in your gym.");
            } else {
                throw new IllegalStateException("This mobile number is already added to your gym.");
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

        member.setMembershipStatus(Member.MembershipStatus.valueOf("ACTIVE"));

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                byte[] compressed = ImageCompressor.compressImage(profilePhoto.getBytes());
                member.setProfilePhoto(compressed);
            } catch (IOException e) {
                throw new RuntimeException("Failed to compress profile photo", e);
            }
        }

        if (dto.getPaymentMethod() != null)
            member.setPaymentMethod(Member.PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase()));

        if (dto.getMembershipPhotoUrl() != null)
            member.setMembershipPhotoUrl(dto.getMembershipPhotoUrl());

        if(dto.getDob() != null)
            member.setDob(dto.getDob());

        if(dto.getGender() != null && !dto.getGender().equals(""))
            member.setGender(Member.Gender.valueOf(dto.getGender().toUpperCase()));

//        if(dto.getHeight() != null)
        if(dto.getHeight() != null )
            member.setHeight(dto.getHeight());

        if(dto.getWeight() != null)
            member.setWeight(dto.getWeight());

        member.setGymOwnerId(dto.getGymOwnerId());
        return memberRepository.save(member);
    }

    @Override
    public boolean deleteMemberByIdAndOwnerId(Long memberId, Long ownerId) {
        Optional<Member> memberOpt = memberRepository.findByIdAndGymOwnerId(memberId, ownerId);
        if (memberOpt.isPresent()) {
            memberRepository.delete(memberOpt.get());
            return true;
        }
        return false;
    }

    @Override
    public void updateMembershipStatus(Long gymOwnerId, Long memberId, Member.MembershipStatus status) {
        Member member = memberRepository.findByIdAndGymOwnerId(memberId, gymOwnerId)
                .orElseThrow(() -> new RuntimeException("Member not found or not authorized"));

        member.setMembershipStatus(status);
        memberRepository.save(member);
    }

    @Override
    public List<Member> getMembersByOwnerAndStatus(Long gymOwnerId, Member.MembershipStatus status) {
        if (status == null) {
            return memberRepository.findByGymOwnerIdOrderByMembershipEndDateAsc(gymOwnerId);
        } else {
            return memberRepository.findByGymOwnerIdAndMembershipStatusOrderByMembershipEndDateAsc(gymOwnerId, status);
        }
    }

    @Override
    public List<Member> searchMembersWithStatus(Long gymOwnerId, String search, Member.MembershipStatus status) {
        if (status == null) {
            return memberRepository.searchByGymOwnerIdAndKeyword(gymOwnerId, search);
        } else {
            return memberRepository.searchByGymOwnerIdAndKeywordAndStatus(gymOwnerId, search, status);
        }
    }

    @Override
    public Member updateMember(MemberRequestDto dto, MultipartFile profilePhoto) {
        // Fetch existing member using ID and gymOwnerId
        Member existing = memberRepository.findByIdAndGymOwnerId(dto.getMemberId(), dto.getGymOwnerId())
                .orElseThrow(() -> new NoSuchElementException("Member not found or does not belong to your gym."));

        // Update fields
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getJoiningDate() != null) existing.setJoiningDate(dto.getJoiningDate());
        if (dto.getMembershipEndDate() != null) existing.setMembershipEndDate(dto.getMembershipEndDate());
        if (dto.getPackageName() != null) existing.setPackageName(dto.getPackageName());
        if (dto.getPaymentStatus() != null)
            existing.setPaymentStatus(Member.PaymentStatus.valueOf(dto.getPaymentStatus().toUpperCase()));
        if (dto.getAmountPaid() != null){
            existing.setAmountPaid(dto.getAmountPaid());
            existing.setMembershipStatus(Member.MembershipStatus.valueOf("ACTIVE"));
        }
        if (dto.getMobileNumber() != null) existing.setMobileNumber(dto.getMobileNumber());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getMembershipStatus() != null)
            existing.setMembershipStatus(Member.MembershipStatus.valueOf(dto.getMembershipStatus().toUpperCase()));
        if (dto.getProfilePhotoUrl() != null) existing.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        if (dto.getPaymentMethod() != null)
            existing.setPaymentMethod(Member.PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase()));
        if (dto.getMembershipPhotoUrl() != null) existing.setMembershipPhotoUrl(dto.getMembershipPhotoUrl());
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                byte[] compressed = ImageCompressor.compressImage(profilePhoto.getBytes());
                existing.setProfilePhoto(compressed);
            } catch (IOException e) {
                throw new RuntimeException("Failed to compress profile photo", e);
            }
        }
        if(dto.getGender() != null) existing.setGender(Member.Gender.valueOf(dto.getGender().toUpperCase()));
        if(dto.getHeight() != null )
            existing.setHeight(dto.getHeight());

        if(dto.getWeight() != null)
            existing.setWeight(dto.getWeight());
        if(dto.getMembershipRenewDate() != null)
            existing.setMembershipRenewDate(dto.getMembershipRenewDate());
        return memberRepository.save(existing);
    }

    @Override
    public AnalysisDTO analysisMembers(Long gymOwnerId) {
        List<Member> members = memberRepository.findByGymOwnerId(gymOwnerId);

        int totalMembers = members.size();
        int activeMembers = (int) members.stream()
                .filter(m -> m.getMembershipStatus() == Member.MembershipStatus.ACTIVE)
                .count();
        int suspendedMembers = (int) members.stream()
                .filter(m -> m.getMembershipStatus() == Member.MembershipStatus.SUSPENDED)
                .count();

        AnalysisDTO analysisDTO = new AnalysisDTO();
        analysisDTO.setTotalMembers(totalMembers);
        analysisDTO.setActiveMembers(activeMembers);
        analysisDTO.setSuspendedMembers(suspendedMembers);

        return analysisDTO;
    }

    @Override
    public void sendExcelToEmail(MultipartFile file, String owner){

    }
}
