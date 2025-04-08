package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.StaffRequestDto;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.entity.Staff;
import com.example.gym_saas_backend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Staff addStaff(StaffRequestDto dto) {
        Optional<Staff> existingStaffOpt = staffRepository
                .findByGymOwnerIdAndMobileNumberOrEmail(dto.getGymOwnerId(), dto.getMobileNumber(), dto.getEmail());

        if (existingStaffOpt.isPresent()) {
            Staff existing = existingStaffOpt.get();
            if (existing.getStatus() == Staff.Status.INACTIVE) {
                throw new IllegalStateException("This staff is inactive in your gym.");
            } else {
                throw new IllegalStateException("This staff is already added to your gym.");
            }
        }
        Staff staff = new Staff();
        staff.setName(dto.getName());
        if(staff.getEmail()!=null) staff.setEmail(dto.getEmail());
        staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        staff.setMobileNumber(dto.getMobileNumber());
        staff.setJoiningDate(dto.getJoinDate());
        staff.setStatus(Staff.Status.valueOf("ACTIVE"));
        staff.setGymOwnerId(dto.getGymOwnerId());
        if(staff.getProfilePhotoUrl()!=null) staff.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        staff.setPasswordUpdated(false);
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(StaffRequestDto dto) {
        Staff staff = staffRepository.findByIdAndGymOwnerId(dto.getStaffId(), dto.getGymOwnerId())
                .orElseThrow(() -> new NoSuchElementException("Staff not found"));

        if(dto.getName()!=null) staff.setName(dto.getName());
        if(dto.getEmail()!=null) staff.setEmail(dto.getEmail());
        if(dto.getPassword()!=null) staff.setPassword(dto.getPassword());
        if(dto.getMobileNumber()!=null) staff.setMobileNumber(dto.getMobileNumber());
        if(dto.getJoinDate()!=null) staff.setJoiningDate(dto.getJoinDate());
        if(dto.getStatus()!=null) staff.setStatus(Staff.Status.valueOf(dto.getStatus().toUpperCase()));
        if(dto.getProfilePhotoUrl()!=null) staff.setProfilePhotoUrl(dto.getProfilePhotoUrl());
        return staffRepository.save(staff);
    }


    @Override
    public boolean deleteStaffByIdAndOwnerId(Long staffId, Long gymOwnerId) {
        Optional<Staff> optional = staffRepository.findByIdAndGymOwnerId(staffId, gymOwnerId);
        if (optional.isPresent()) {
            staffRepository.delete(optional.get());
            return true;
        }
        return false;
    }

    @Override
    public void updateStaffStatus(Long gymOwnerId, Long staffId, String status) {
        Staff staff = staffRepository.findByIdAndGymOwnerId(staffId, gymOwnerId)
                .orElseThrow(() -> new NoSuchElementException("Staff not found"));
        staff.setStatus(Staff.Status.valueOf(status.toUpperCase()));
        staffRepository.save(staff);
    }

    @Override
    public void updatePassword(Long staffId, Long gymOwnerId, String newPassword) {
        Staff staff = staffRepository.findByIdAndGymOwnerId(staffId, gymOwnerId).orElseThrow(() -> new NoSuchElementException("Staff not found"));
        staff.setPassword(passwordEncoder.encode(newPassword)); // Assuming passwordEncoder is autowired
        staff.setPasswordUpdated(true);
        staffRepository.save(staff);
    }

    @Override
    public List<Staff> getStaffByOwnerAndStatus(Long gymOwnerId, Staff.Status status) {
        if (status == null) {
            return staffRepository.findByGymOwnerIdOrderByStatusAsc(gymOwnerId);
        } else {
            return staffRepository.findByGymOwnerIdAndStatusOrderByStatusAsc(gymOwnerId, status);
        }
    }

    @Override
    public List<Staff> searchStaffWithStatus(Long gymOwnerId, String search, Staff.Status status) {
        if (status == null) {
            return staffRepository.searchByGymOwnerIdAndKeyword(gymOwnerId, search);
        } else {
            return staffRepository.searchByGymOwnerIdAndKeywordAndStatus(gymOwnerId, search, status);
        }
    }
}
