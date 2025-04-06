package com.example.gym_saas_backend.controller;

import com.example.gym_saas_backend.dto.ApiResponse;
import com.example.gym_saas_backend.dto.MemberRequestDto;
import com.example.gym_saas_backend.entity.Member;
import com.example.gym_saas_backend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

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
                                                                          @RequestParam(required = false) String search){
        try {
            Long gymOwnerId = (Long) request.getAttribute("gymOwnerId");
            List<Member> members;
            if (search != null && !search.trim().isEmpty()) {
                members = memberService.searchMembers(gymOwnerId, search);
            } else {
                members = memberService.getMembersByOwner(gymOwnerId);
            }

            if (members.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(new ApiResponse<>(false, "No members found for this gym owner.", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Members fetched successfully", members));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(false, "Failed to retrieve members", null));
        }
    }

}
