package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.ApiResponse;
import com.example.gym_saas_backend.dto.OwnerResponseDTO;
import com.example.gym_saas_backend.dto.OwnerAnalysisDTO;
import com.example.gym_saas_backend.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/owners")
    public ResponseEntity<ApiResponse<List<OwnerResponseDTO>>> getOwners(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer joinMonth,
            @RequestParam(required = false) Integer joinYear,
            @RequestParam(required = false) String status
    ) {
        try {
            List<OwnerResponseDTO> owners = adminService.getOwners(search, joinMonth, joinYear, status);
            return ResponseEntity.ok(new ApiResponse<>(true, "Owners fetched successfully", owners));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to fetch owners: " + e.getMessage(), null));
        }
    }

    @GetMapping("/analysis")
    public OwnerAnalysisDTO getOwnerAnalysis() {
        return adminService.getOwnerAnalysis();
    }
}
