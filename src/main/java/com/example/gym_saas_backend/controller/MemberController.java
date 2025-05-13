package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.AnalysisDTO;
import com.example.gym_saas_backend.dto.ApiResponse;
import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.dto.UpdateMembershipStatusRequest;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.entity.Owner;
import com.example.gym_saas_backend.service.MemberService;
import com.example.gym_saas_backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Member>> addMember(HttpServletRequest request,
                                                         @RequestPart("dto") MemberRequestDto dto,
                                                         @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            dto.setGymOwnerId(gymOwnerId);
            Member member = memberService.addMember(dto, profilePhoto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Member added successfully", member));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Something went wrong: " + e.getMessage(), null));
        }
    }

    @GetMapping("/by-owner")
    public ResponseEntity<ApiResponse<List<Member>>> getMembersByGymOwner(HttpServletRequest request,
                                                                          @RequestParam(required = false) String search,
                                                                          @RequestParam(required = false) String status) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            List<Member> members;

            // Normalize status
            Member.MembershipStatus statusEnum = null;
            if (status != null && !status.equalsIgnoreCase("BOTH")) {
                try {
                    statusEnum = Member.MembershipStatus.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(false, "Invalid membership status" + e.getMessage(), null));
                }
            }

            if (search != null && !search.trim().isEmpty()) {
                members = memberService.searchMembersWithStatus(gymOwnerId, search, statusEnum);
            } else {
                members = memberService.getMembersByOwnerAndStatus(gymOwnerId, statusEnum);
            }

            if (members.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(new ApiResponse<>(false, "No members found.", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Members fetched successfully", members));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(false, "Failed to retrieve members" + e.getMessage(), null));
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteMember(HttpServletRequest request,
                                                            @RequestBody Map<String, Long> payload) {
        try {
            Long memberId = payload.get("memberId");
            if (memberId == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Member ID is required",null));
            }

            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");

            boolean deleted = memberService.deleteMemberByIdAndOwnerId(memberId, gymOwnerId);

            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Member deleted successfully",null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Member not found or unauthorized",null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Deletion failed: " + e.getMessage(),null));
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<ApiResponse<String>> updateMembershipStatus(@RequestBody UpdateMembershipStatusRequest requestBody,
                                                                      HttpServletRequest request) {
        try {

            String role = (String) request.getAttribute("role");
            System.out.println(role);
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");

            memberService.updateMembershipStatus(gymOwnerId, requestBody.getMemberId(), requestBody.getMembershipStatus());

            return ResponseEntity.ok(new ApiResponse<>(true, "Membership status updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to update membership status: " + e.getMessage(), null));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Member>> updateMember(HttpServletRequest request,
                                                            @RequestPart("dto") MemberRequestDto dto,
                                                            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            dto.setGymOwnerId(gymOwnerId);
            Member updatedMember = memberService.updateMember(dto, profilePhoto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Member updated successfully", updatedMember));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Something went wrong" + e.getMessage(), null));
        }
    }

    @GetMapping("/analysis")
    public ResponseEntity<ApiResponse<AnalysisDTO>> analysisMembers(HttpServletRequest request){
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            AnalysisDTO analysisDTO = memberService.analysisMembers(gymOwnerId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Member analyze successfully", analysisDTO));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Something went wrong" + e.getMessage(), null));
        }
    }

    @PostMapping("/import-members")
    public ResponseEntity<String> importMembers(
            @RequestPart("excelFile") MultipartFile file,
            @RequestPart("ownerId") String ownerId,
            @RequestPart("name") String name,
            @RequestPart("mobileNumber") String mobileNumber
    )  {
        try {
            boolean isSent = memberService.sendExcelToEmail(file, ownerId, name, mobileNumber);
            if (isSent) {
                return ResponseEntity.ok("Email sent successfully.");
            } else {
                return ResponseEntity.status(500).body("Failed to send email.");
            }
        } catch (Exception e) {

            return ResponseEntity.status(500).body("Failed to send Excel file: " + e.getMessage());
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, @RequestParam(required = true) String mobileNumber) {
        Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
        String role = (String) request.getAttribute("role");

        String newAccessToken = jwtUtil.generateToken(mobileNumber, role, gymOwnerId);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

}
