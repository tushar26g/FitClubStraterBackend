package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.dto.OwnerResponseDTO;
import com.example.gym_saas_backend.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmailOrMobileNumber(String email, String mobileNumber);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);

    @Query("SELECT new com.example.gym_saas_backend.dto.OwnerResponseDTO(" +
            "o.id, o.fullName, o.email, o.mobileNumber, o.businessName, " +
            "o.accountStatus, o.membershipEndDate, o.createdAt) " +
            "FROM Owner o " +
            "WHERE " +
            "(:search IS NULL OR " +
            "LOWER(o.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(o.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(o.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:joinMonth IS NULL OR MONTH(o.createdAt) = :joinMonth) " +
            "AND (:joinYear IS NULL OR YEAR(o.createdAt) = :joinYear) " +
            "AND (:status IS NULL OR o.accountStatus = :status) " +
            "ORDER BY o.membershipEndDate ASC")
    List<OwnerResponseDTO> searchOwnersForAdmin(
            String search,
            Integer joinMonth,
            Integer joinYear,
            String status
    );


    @Query("SELECT COUNT(o) FROM Owner o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    Long countOwnersJoinedInMonth(int month, int year);

}

