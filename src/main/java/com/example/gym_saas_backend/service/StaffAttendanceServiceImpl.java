package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.LeaveRequestDTO;
import com.example.gym_saas_backend.dto.StaffAttendanceDTO;
import com.example.gym_saas_backend.entity.StaffAttendance;
import com.example.gym_saas_backend.dto.StaffAttendanceResponseDTO;
import com.example.gym_saas_backend.repository.StaffAttendanceRepository;
import com.example.gym_saas_backend.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StaffAttendanceServiceImpl implements StaffAttendanceService {

    @Autowired
    private StaffAttendanceRepository repo;

    @Autowired
    private StaffRepository staffRepo;

    @Override
    public void markAttendance(Long gymOwnerId, StaffAttendanceDTO dto) {
        LocalDate date = dto.getDate() != null ? dto.getDate() : LocalDate.now();

        Optional<StaffAttendance> existing = repo.findByStaffIdAndDate(dto.getStaffId(), date);
//        if (existing.isPresent()) {
//            throw new RuntimeException("Attendance already marked");
//        }

        StaffAttendance att = new StaffAttendance();
        att.setStaffId(dto.getStaffId());
        att.setGymOwnerId(gymOwnerId);
        att.setDate(date);
        att.setStatus(StaffAttendance.Status.valueOf(dto.getStatus().toUpperCase()));
        att.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        repo.save(att);
    }

    @Override
    public void markLeaveRange(Long gymOwnerId, LeaveRequestDTO dto) {
        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            boolean alreadyMarked = repo.findByStaffIdAndDate(dto.getStaffId(), date).isPresent();
            if (alreadyMarked) continue;

            StaffAttendance att = new StaffAttendance();
            att.setStaffId(dto.getStaffId());
            att.setGymOwnerId(gymOwnerId);
            att.setDate(date);
            att.setStatus(StaffAttendance.Status.LEAVE);
            att.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            repo.save(att);
        }
    }

    @Override
    public List<StaffAttendanceResponseDTO> getHistory(Long gymOwnerId, String search, LocalDate start, LocalDate end) {
        return repo.searchWithFilters(gymOwnerId, search == null ? "" : search, start, end);
    }

    public List<StaffAttendance> getStaffAttendanceHistory(Long gymOwnerId, Long staffId) {
        return repo.getAttendanceForStaff(gymOwnerId, staffId);
    }

}
