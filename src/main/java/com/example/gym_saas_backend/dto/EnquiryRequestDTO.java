package com.example.gym_saas_backend.dto;

import com.example.gym_saas_backend.entity.Enquiry;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EnquiryRequestDTO {
    private String name;
    private String mobileNumber;
    private String email;
    private Enquiry.InterestLevel interestLevel;
    private LocalDate enquiryDate;
    private Long gymOwnerId;

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

    public Enquiry.InterestLevel getInterestLevel() {
        return interestLevel;
    }

    public void setInterestLevel(Enquiry.InterestLevel interestLevel) {
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
}
