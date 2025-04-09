package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.EnquiryRequestDTO;
import com.example.gym_saas_backend.dto.EnquiryResponseDTO;
import com.example.gym_saas_backend.entity.Enquiry;
import com.example.gym_saas_backend.repository.EnquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnquiryServiceImpl implements EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Override
    public void addEnquiry(EnquiryRequestDTO dto) {
        Enquiry enquiry = new Enquiry();
        enquiry.setName(dto.getName());
        enquiry.setMobileNumber(dto.getMobileNumber());
        enquiry.setEmail(dto.getEmail());
        enquiry.setInterestLevel(dto.getInterestLevel());
        enquiry.setEnquiryDate(dto.getEnquiryDate());
        enquiry.setGymOwnerId(dto.getGymOwnerId());
        enquiryRepository.save(enquiry);
    }

    @Override
    public List<EnquiryResponseDTO> getEnquiries(String search, Enquiry.InterestLevel interestLevel, Long gymOwnerId) {
        return enquiryRepository.findEnquiriesWithFilters(search, interestLevel, gymOwnerId);
    }

    @Override
    public boolean deleteEnquiryByIdAndOwnerId(Long enquiryId, Long gymOwnerId) {
        Optional<Enquiry> enquiryOpt = enquiryRepository.findByIdAndGymOwnerId(enquiryId, gymOwnerId);
        if (enquiryOpt.isPresent()) {
            enquiryRepository.delete(enquiryOpt.get());
            return true;
        }
        return false;
    }

}
