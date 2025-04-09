package com.example.gym_saas_backend.service;

import com.example.gym_saas_backend.dto.OwnerResponseDTO;
import com.example.gym_saas_backend.dto.OwnerAnalysisDTO;

import java.util.List;

public interface AdminService {
    List<OwnerResponseDTO> getOwners(String search, Integer joinMonth, Integer joinYear, String status);
    OwnerAnalysisDTO getOwnerAnalysis();
}
