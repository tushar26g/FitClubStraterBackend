package com.example.gym_saas_backend.dto;

import com.example.gym_saas_backend.entity.Staff;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StaffAttendanceResponseDTO {
    private Long attendanceId;
    private Long staffId;
    private LocalDate date;
    private String attendanceType;

    private String name;
    private String mobileNumber;
    private String email;
    private LocalDate joiningDate;
    private Long gymOwnerId;
    private Staff.Status status;
    private String profilePhotoUrl;
    private LocalDateTime createdAt;

    public StaffAttendanceResponseDTO(Long attendanceId, Long staffId, LocalDate date, String attendanceType,
                                      String name, String mobileNumber, String email, LocalDate joiningDate,
                                      Long gymOwnerId, Staff.Status status, String profilePhotoUrl, LocalDateTime createdAt) {
        this.attendanceId = attendanceId;
        this.staffId = staffId;
        this.date = date;
        this.attendanceType = attendanceType;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.joiningDate = joiningDate;
        this.gymOwnerId = gymOwnerId;
        this.status = status;
        this.profilePhotoUrl = profilePhotoUrl;
        this.createdAt = createdAt;
    }

    // Add getters/setters or use Lombok

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Long getGymOwnerId() {
        return gymOwnerId;
    }

    public void setGymOwnerId(Long gymOwnerId) {
        this.gymOwnerId = gymOwnerId;
    }

    public Staff.Status getStatus() {
        return status;
    }

    public void setStatus(Staff.Status status) {
        this.status = status;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
