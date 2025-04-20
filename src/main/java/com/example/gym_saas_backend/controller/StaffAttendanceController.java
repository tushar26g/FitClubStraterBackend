package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.ApiResponse;
import com.example.gym_saas_backend.dto.LeaveRequestDTO;
import com.example.gym_saas_backend.dto.StaffAttendanceDTO;
import com.example.gym_saas_backend.dto.StaffAttendanceResponseDTO;
import com.example.gym_saas_backend.entity.StaffAttendance;
import com.example.gym_saas_backend.service.StaffAttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class StaffAttendanceController {

    @Autowired
    private StaffAttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<ApiResponse<String>> markAttendance(
            HttpServletRequest request,
            @RequestBody StaffAttendanceDTO dto) {

        Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
        attendanceService.markAttendance(gymOwnerId, dto);

        return ResponseEntity.ok(new ApiResponse<>(true, "Attendance marked", null));
    }

    @PostMapping("/mark-leave")
    public ResponseEntity<ApiResponse<String>> markLeaveRange(
            HttpServletRequest request,
            @RequestBody LeaveRequestDTO dto) {

        Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
        attendanceService.markLeaveRange(gymOwnerId, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Leave marked successfully", null));
    }

    @GetMapping("/history")
    public ResponseEntity<List<StaffAttendanceResponseDTO>> getAttendanceHistory(
            HttpServletRequest request,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
        return ResponseEntity.ok(attendanceService.getHistory(gymOwnerId, search, startDate, endDate));
    }

    @GetMapping("/staff/{staffId}/attendance")
    public ResponseEntity<ApiResponse<List<StaffAttendance>>> getStaffAttendance(
            HttpServletRequest request,
            @PathVariable Long staffId
    ) {
        Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
        List<StaffAttendance> attendance = attendanceService.getStaffAttendanceHistory(gymOwnerId, staffId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched successfully", attendance));
    }

}
