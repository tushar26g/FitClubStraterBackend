package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.OwnerUpdateDto;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.other.ImageCompressor;
import com.example.gym_saas_backend.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;
    public Owner updateOwnerDetails(Long ownerId, OwnerUpdateDto dto, MultipartFile profilePhoto) {
        Owner existingOwner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        // ✅ Check for duplicate mobile number (exclude current owner)
        if (dto.getMobileNumber() != null &&
                ownerRepository.existsByMobileNumberAndIdNot(dto.getMobileNumber(), ownerId)) {
            throw new RuntimeException("Mobile number is already in use by another account.");
        }

        // ✅ Check for duplicate email (exclude current owner)
        if (dto.getEmail() != null &&
                ownerRepository.existsByEmailAndIdNot(dto.getEmail(), ownerId)) {
            throw new RuntimeException("Email is already in use by another account.");
        }

        if (dto.getFullName() != null) existingOwner.setFullName(dto.getFullName());
        if (dto.getMobileNumber() != null) existingOwner.setMobileNumber(dto.getMobileNumber());
        if (dto.getEmail() != null) existingOwner.setEmail(dto.getEmail());
        if (dto.getBusinessName() != null) existingOwner.setBusinessName(dto.getBusinessName());
        if (dto.getAddress() != null) existingOwner.setAddress(dto.getAddress());

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                byte[] compressed = ImageCompressor.compressImage(profilePhoto.getBytes());
                existingOwner.setProfilePhoto(compressed);
            } catch (IOException e) {
                throw new RuntimeException("Failed to compress profile photo", e);
            }
        }

        return ownerRepository.save(existingOwner);
    }

}
