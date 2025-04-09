package com.example.gym_saas_backend.dto;

import com.example.gym_saas_backend.entity.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OwnerResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String businessName;
    private Owner.AccountStatus accountStatus;
    private LocalDate membershipEndDate;
    private LocalDateTime createdAt;

    public OwnerResponseDTO(Long id, String fullName, String email, String mobileNumber,
                            String businessName, Owner.AccountStatus accountStatus,
                            LocalDate membershipEndDate, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.businessName = businessName;
        this.accountStatus = accountStatus;
        this.membershipEndDate = membershipEndDate;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Owner.AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Owner.AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LocalDate getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(LocalDate membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
