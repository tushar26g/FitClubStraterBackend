package com.example.gym_saas_backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate joiningDate;
    private LocalDate membershipEndDate;
    private String packageName;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private BigDecimal amountPaid;
    private String mobileNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    private String profilePhotoUrl;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String membershipPhotoUrl;

    private Long gymOwnerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum PaymentStatus {
        PARTIAL, COMPLETED
    }

    public enum MembershipStatus {
        ACTIVE, SUSPENDED
    }

    public enum PaymentMethod {
        CASH, UPI, CARD
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDate getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(LocalDate membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getMembershipPhotoUrl() {
        return membershipPhotoUrl;
    }

    public void setMembershipPhotoUrl(String membershipPhotoUrl) {
        this.membershipPhotoUrl = membershipPhotoUrl;
    }

    public Long getGymOwnerId() {
        return gymOwnerId;
    }

    public void setGymOwnerId(Long gymOwnerId) {
        this.gymOwnerId = gymOwnerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Lob
    @Column(name = "profile_photo", columnDefinition = "LONGBLOB")
    private byte[] profilePhoto;

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Enumerated(EnumType.STRING)
    private Member.Gender gender;

    public enum Gender { MALE, FEMALE, OTHER }

    private LocalDate dateOfBirth;

    public Member.Gender getGender() {
        return gender;
    }

    public void setGender(Member.Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dateOfBirth;
    }

    public void setDob(LocalDate dob) {
        this.dateOfBirth = dob;
    }

    private Double heightCm;

    private Double weightKg;

    public Double getHeight() {
        return heightCm;
    }

    public void setHeight(Double height) {
        this.heightCm = height;
    }

    public Double getWeight() {
        return weightKg;
    }

    public void setWeight(Double weight) {
        this.weightKg = weight;
    }
}
