package com.example.gym_saas_backend.dto;


public class OwnerAnalysisDTO {
    private Long thisMonthJoins;
    private Long lastMonthJoins;
    private Double growthRate;

    public OwnerAnalysisDTO(Long thisMonthJoins, Long lastMonthJoins, Double growthRate) {
        this.thisMonthJoins = thisMonthJoins;
        this.lastMonthJoins = lastMonthJoins;
        this.growthRate = growthRate;
    }

    // Getters and setters

    public Long getThisMonthJoins() {
        return thisMonthJoins;
    }

    public void setThisMonthJoins(Long thisMonthJoins) {
        this.thisMonthJoins = thisMonthJoins;
    }

    public Long getLastMonthJoins() {
        return lastMonthJoins;
    }

    public void setLastMonthJoins(Long lastMonthJoins) {
        this.lastMonthJoins = lastMonthJoins;
    }

    public Double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(Double growthRate) {
        this.growthRate = growthRate;
    }
}

