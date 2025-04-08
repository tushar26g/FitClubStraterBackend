package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.StaffRequestDto;
import com.example.gym_saas_backend.entity.Staff;

public interface StaffService {
    Staff addStaff(StaffRequestDto dto);
    Staff updateStaff(StaffRequestDto dto);
    boolean deleteStaffByIdAndOwnerId(Long staffId, Long gymOwnerId);
    void updateStaffStatus(Long gymOwnerId, Long staffId, String status);
    void updatePassword(Long staffId, Long gymOwnerId, String newPassword);
}
