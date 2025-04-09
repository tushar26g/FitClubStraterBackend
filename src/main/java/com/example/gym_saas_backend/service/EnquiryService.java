package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.EnquiryRequestDTO;
import com.example.gym_saas_backend.dto.EnquiryResponseDTO;
import com.example.gym_saas_backend.entity.Enquiry;

import java.util.List;

public interface EnquiryService {
    void addEnquiry(EnquiryRequestDTO dto);
    List<EnquiryResponseDTO> getEnquiries(String search, Enquiry.InterestLevel interestLevel, Long gymOwnerId);
    boolean deleteEnquiryByIdAndOwnerId(Long enquiryId, Long gymOwnerId);

}
