package com.example.gym_saas_backend.controller;
import com.example.gym_saas_backend.dto.ApiResponse;
import com.example.gym_saas_backend.dto.StaffRequestDto;
import com.example.gym_saas_backend.dto.UpdateStaffStatusRequest;
import com.example.gym_saas_backend.entity.Staff;
import com.example.gym_saas_backend.service.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Staff>> addStaff(HttpServletRequest request,
                                                       @RequestBody StaffRequestDto dto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            dto.setGymOwnerId(gymOwnerId);
            Staff staff = staffService.addStaff(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Staff added successfully", staff));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Something went wrong", null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Staff>> updateStaff(HttpServletRequest request,
                                                          @RequestBody StaffRequestDto dto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            dto.setGymOwnerId(gymOwnerId);
            Staff updated = staffService.updateStaff(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Staff updated successfully", updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Something went wrong", null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteStaff(HttpServletRequest request,
                                                           @RequestBody Map<String, Long> payload) {
        try {
            Long staffId = payload.get("staffId");
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            boolean deleted = staffService.deleteStaffByIdAndOwnerId(staffId, gymOwnerId);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Staff deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Staff not found or unauthorized", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Deletion failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<ApiResponse<String>> updateStatus(HttpServletRequest request,
                                                            @RequestBody UpdateStaffStatusRequest dto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            staffService.updateStaffStatus(gymOwnerId, dto.getStaffId(), dto.getStatus());
            return ResponseEntity.ok(new ApiResponse<>(true, "Staff status updated", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to update status", null));
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(HttpServletRequest request,
                                                              @RequestBody Map<String, String> body) {
        try {
            String newPassword = body.get("newPassword");
            Long staffId = (Long) request.getAttribute("staffId"); // You need to extract this via JWT
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            staffService.updatePassword(staffId, gymOwnerId, newPassword);
            return ResponseEntity.ok(new ApiResponse<>(true, "Password updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(false, "Password update failed: " + e.getMessage(), null));
        }
    }

}

