package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.LeaveRequestDTO;
import com.example.gym_saas_backend.dto.StaffAttendanceDTO;
import com.example.gym_saas_backend.dto.StaffAttendanceResponseDTO;
import com.example.gym_saas_backend.entity.StaffAttendance;

import java.time.LocalDate;
import java.util.List;

public interface StaffAttendanceService {
    void markAttendance(Long gymOwnerId, StaffAttendanceDTO dto);
    void markLeaveRange(Long gymOwnerId, LeaveRequestDTO dto);
    List<StaffAttendanceResponseDTO> getHistory(Long gymOwnerId, String search, LocalDate startDate, LocalDate endDate);
    List<StaffAttendanceResponseDTO> getStaffAttendanceHistory(Long gymOwnerId, Long staffId, LocalDate start, LocalDate end);
}

