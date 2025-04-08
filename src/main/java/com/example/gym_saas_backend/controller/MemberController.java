package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.ApiResponse;
import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.dto.UpdateMembershipStatusRequest;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.service.MemberService;
import com.example.gym_saas_backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Member>> addMember(HttpServletRequest request,
                                                         @RequestBody MemberRequestDto dto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            dto.setGymOwnerId(gymOwnerId);
            Member member = memberService.addMember(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Member added successfully", member));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Something went wrong", null));
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
                            .body(new ApiResponse<>(false, "Invalid membership status", null));
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
                    .body(new ApiResponse<>(false, "Failed to retrieve members", null));
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
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(jwt)
//                    .getBody();

            String role = (String) request.getAttribute("role");
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
                                                            @RequestBody MemberRequestDto dto) {
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            dto.setGymOwnerId(gymOwnerId);
            Member updatedMember = memberService.updateMember(dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Member updated successfully", updatedMember));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Something went wrong", null));
        }
    }

}
