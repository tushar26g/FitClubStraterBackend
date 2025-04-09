package com.example.gym_saas_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "enquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mobileNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private InterestLevel interestLevel;

    private LocalDate enquiryDate;

    private Long gymOwnerId;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum InterestLevel {
        HIGH, MODERATE, LOW
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

    public InterestLevel getInterestLevel() {
        return interestLevel;
    }

    public void setInterestLevel(InterestLevel interestLevel) {
        this.interestLevel = interestLevel;
    }

    public LocalDate getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(LocalDate enquiryDate) {
        this.enquiryDate = enquiryDate;
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
}
