package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.StaffRequestDto;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.entity.Staff;

import java.util.List;

public interface StaffService {
    Staff addStaff(StaffRequestDto dto);
    Staff updateStaff(StaffRequestDto dto);
    boolean deleteStaffByIdAndOwnerId(Long staffId, Long gymOwnerId);
    void updateStaffStatus(Long gymOwnerId, Long staffId, String status);
//    void updatePassword(Long staffId, Long gymOwnerId, String newPassword);
    List<Staff> searchStaffWithStatus(Long gymOwnerId, String search, Staff.Status status);
    List<Staff> getStaffByOwnerAndStatus(Long gymOwnerId, Staff.Status status);
}
