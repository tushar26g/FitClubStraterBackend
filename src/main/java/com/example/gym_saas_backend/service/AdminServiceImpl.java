package com.example.gym_saas_backend.service.impl;

import com.example.gym_saas_backend.dto.OwnerResponseDTO;
import com.example.gym_saas_backend.dto.OwnerAnalysisDTO;
import com.example.gym_saas_backend.repository.OwnerRepository;
import com.example.gym_saas_backend.service.AdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final OwnerRepository ownerRepository;

    public AdminServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<OwnerResponseDTO> getOwners(String search, Integer joinMonth, Integer joinYear, String status) {
        return ownerRepository.searchOwnersForAdmin(search, joinMonth, joinYear, status);
    }

    @Override
    public OwnerAnalysisDTO getOwnerAnalysis() {
        LocalDate now = LocalDate.now();
        int thisMonth = now.getMonthValue();
        int thisYear = now.getYear();

        LocalDate lastMonthDate = now.minusMonths(1);
        int lastMonth = lastMonthDate.getMonthValue();
        int lastYear = lastMonthDate.getYear();

        Long thisMonthCount = ownerRepository.countOwnersJoinedInMonth(thisMonth, thisYear);
        Long lastMonthCount = ownerRepository.countOwnersJoinedInMonth(lastMonth, lastYear);

        double growthRate = 0.0;
        if (lastMonthCount != 0) {
            growthRate = ((thisMonthCount - lastMonthCount) / (double) lastMonthCount) * 100.0;
        } else if (thisMonthCount > 0) {
            growthRate = 100.0;
        }

        return new OwnerAnalysisDTO(thisMonthCount, lastMonthCount, growthRate);
    }
}
