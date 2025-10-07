package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.OwnerUpdateDto;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.service.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @PostMapping("/update")
    public ResponseEntity<?> updateOwner(@RequestPart("dto") OwnerUpdateDto dto,
                                         @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
                                         HttpServletRequest request) {
        try {
            Long ownerId = (Long) request.getAttribute("gymOwnerId"); // Set by JWT filter

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                // Optional: Check file size (security)
                if (profilePhoto.getSize() > 5_000_000) // 5MB, adjust as needed
                    return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Photo is too large"));

                // Optional: Check content type (flexible)
                String type = profilePhoto.getContentType();
                if (type == null || !type.startsWith("image/")) {
                    return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid image file type"));
                }
            }

            Owner updatedOwner = ownerService.updateOwnerDetails(ownerId, dto, profilePhoto);
            return ResponseEntity.ok(Map.of("success", true, "updatedOwner", updatedOwner));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }


}
