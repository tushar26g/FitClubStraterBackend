package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.entity.StaffAttendance;
import com.example.gym_saas_backend.dto.StaffAttendanceResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, Long> {

    Optional<StaffAttendance> findByStaffIdAndDate(Long staffId, LocalDate date);

    List<StaffAttendance> findByGymOwnerIdAndDate(Long gymOwnerId, LocalDate date);

    @Query("SELECT new com.example.gym_saas_backend.dto.StaffAttendanceResponseDTO(" +
            "a.id, a.staffId, a.date, CAST(a.status AS string), " +
            "s.name, s.mobileNumber, s.email, s.joiningDate, " +
            "s.gymOwnerId, s.status, s.profilePhotoUrl, s.createdAt) " +
            "FROM StaffAttendance a " +
            "JOIN Staff s ON a.staffId = s.id " +
            "WHERE a.gymOwnerId = :gymOwnerId " +
            "AND (" +
            "   :search IS NULL OR " +
            "   LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "   LOWER(s.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "   LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))" +
            ") " +
            "AND (:startDate IS NULL OR a.date >= :startDate) " +
            "AND (:endDate IS NULL OR a.date <= :endDate) " +
            "ORDER BY a.date DESC")
    List<StaffAttendanceResponseDTO> searchWithFilters(
            Long gymOwnerId,
            String search,
            LocalDate startDate,
            LocalDate endDate
    );



    @Query("SELECT a " +  // Casting enum to Strin
            "FROM StaffAttendance a " +
            "WHERE a.gymOwnerId = :gymOwnerId AND a.staffId = :staffId ")
    List<StaffAttendance> getAttendanceForStaff(
            Long gymOwnerId,
            Long staffId
    );
}
