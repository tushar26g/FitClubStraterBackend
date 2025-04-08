package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.StaffRequestDto;
import com.example.gym_saas_backend.entity.Staff;
import com.example.gym_saas_backend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        Staff staff = new Staff();
        staff.setName(dto.getName());
        staff.setEmail(dto.getEmail());
        staff.setPassword(dto.getPassword());
        staff.setMobileNumber(dto.getMobileNumber());
        staff.setJoiningDate(dto.getJoinDate());
        staff.setStatus(Staff.Status.valueOf(dto.getStatus().toUpperCase()));
        staff.setGymOwnerId(dto.getGymOwnerId());
        staff.setProfilePhotoUrl(dto.getProfilePhotoUrl());
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

    public void updatePassword(Long staffId, Long gymOwnerId, String newPassword) {
        Staff staff = staffRepository.findByIdAndGymOwnerId(staffId, gymOwnerId).orElseThrow(() -> new NoSuchElementException("Staff not found"));
        staff.setPassword(passwordEncoder.encode(newPassword)); // Assuming passwordEncoder is autowired
        staff.setPasswordUpdated(true);
        staffRepository.save(staff);
    }

}
