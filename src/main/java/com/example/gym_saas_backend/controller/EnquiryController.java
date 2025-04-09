package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.ApiResponse;
import com.example.gym_saas_backend.dto.EnquiryRequestDTO;
import com.example.gym_saas_backend.dto.EnquiryResponseDTO;
import com.example.gym_saas_backend.entity.Enquiry;
import com.example.gym_saas_backend.service.EnquiryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addEnquiry(HttpServletRequest request, @RequestBody EnquiryRequestDTO dto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            dto.setGymOwnerId(gymOwnerId);
            enquiryService.addEnquiry(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Enquiry added successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to add enquiry", null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnquiryResponseDTO>>> getEnquiries(
            HttpServletRequest request,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Enquiry.InterestLevel interestLevel
    ) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            List<EnquiryResponseDTO> list = enquiryService.getEnquiries(search, interestLevel, gymOwnerId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Enquiries fetched successfully", list));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to fetch enquiries", null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteEnquiry(HttpServletRequest request,
                                                             @RequestBody Map<String, Long> payload) {
        try {
            Long enquiryId = payload.get("enquiryId");
            if (enquiryId == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Enquiry ID is required", null));
            }

            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");

            boolean deleted = enquiryService.deleteEnquiryByIdAndOwnerId(enquiryId, gymOwnerId);

            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Enquiry deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Enquiry not found or unauthorized", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Deletion failed: " + e.getMessage(), null));
        }
    }

}
