package com.example.gym_saas_backend.dto;

import com.example.gym_saas_backend.entity.Member;

public class UpdateMembershipStatusRequest {
    private Long memberId;
    private Member.MembershipStatus membershipStatus;

    // Getters and setters

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Member.MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(Member.MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }
}
