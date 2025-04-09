package com.example.gym_saas_backend.dto;

import java.time.LocalDate;

public class StaffAttendanceDTO {
    private Long staffId;
    private String status; // "PRESENT", "ABSENT", "LEAVE"
    private LocalDate date;

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
