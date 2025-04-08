package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByIdAndGymOwnerId(Long id, Long gymOwnerId);

    @Query("SELECT m FROM Staff m WHERE m.gymOwnerId = :gymOwnerId AND (m.mobileNumber = :mobileNumber OR m.email = :email)")
    Optional<Staff> findByGymOwnerIdAndMobileNumberOrEmail(Long gymOwnerId, String mobileNumber, String email);

    // 1. Get all staff for a gym owner, ordered by status
    List<Staff> findByGymOwnerIdOrderByStatusAsc(Long gymOwnerId);

    // 2. Get staff by gym owner and status, ordered by status
    List<Staff> findByGymOwnerIdAndStatusOrderByStatusAsc(Long gymOwnerId, Staff.Status status);

    // 1. Search staff by gym owner and keyword (name, mobile, email) — sorted by status
    @Query("SELECT s FROM Staff s WHERE s.gymOwnerId = :gymOwnerId AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY s.status ASC")
    List<Staff> searchByGymOwnerIdAndKeyword(
            Long gymOwnerId,
            String search);

    // 2. Search staff by gym owner, keyword and status — sorted by status
    @Query("SELECT s FROM Staff s WHERE s.gymOwnerId = :gymOwnerId AND s.status = :status AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY s.status ASC")
    List<Staff> searchByGymOwnerIdAndKeywordAndStatus(
            Long gymOwnerId,
            String search,
            Staff.Status status);
}
