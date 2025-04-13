package com.example.gym_saas_backend.dto;

import lombok.Data;

@Data
public class AnalysisDTO {
    private int totalMembers;
    private int activeMembers;
    private int suspendedMembers;

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getActiveMembers() {
        return activeMembers;
    }

    public void setActiveMembers(int activeMembers) {
        this.activeMembers = activeMembers;
    }

    public int getSuspendedMembers() {
        return suspendedMembers;
    }

    public void setSuspendedMembers(int suspendedMembers) {
        this.suspendedMembers = suspendedMembers;
    }
}
