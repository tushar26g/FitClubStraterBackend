package com.example.gym_saas_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

    public enum Status {
        ACTIVE, INACTIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "mobile_number")
    private String mobileNumber;

    private String email;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "gym_owner_id")
    private Long gymOwnerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "profile_photo_url", columnDefinition = "TEXT")
    private String profilePhotoUrl;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    @Lob
    @Column(name = "profile_photo", columnDefinition = "LONGBLOB")
    private byte[] profilePhoto;

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Enumerated(EnumType.STRING)
    private Staff.Gender gender;

    public enum Gender { MALE, FEMALE, OTHER }

    private LocalDate dateOfBirth;

    public Staff.Gender getGender() {
        return gender;
    }

    public void setGender(Staff.Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dateOfBirth;
    }

    public void setDob(LocalDate dob) {
        this.dateOfBirth = dob;
    }
}
