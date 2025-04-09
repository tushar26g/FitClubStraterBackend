package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.dto.EnquiryResponseDTO;
import com.example.gym_saas_backend.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    @Query("SELECT new com.example.gym_saas_backend.dto.EnquiryResponseDTO(" +
            "e.id, e.name, e.mobileNumber, e.email, e.interestLevel, e.enquiryDate, e.createdAt) " +
            "FROM Enquiry e " +
            "WHERE (:search IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR e.mobileNumber LIKE CONCAT('%', :search, '%')) " +
            "AND (:interestLevel IS NULL OR e.interestLevel = :interestLevel) " +
            "AND e.gymOwnerId = :gymOwnerId " +
            "ORDER BY e.createdAt DESC")
    List<EnquiryResponseDTO> findEnquiriesWithFilters(
            String search,
            Enquiry.InterestLevel interestLevel,
            Long gymOwnerId
    );

    Optional<Enquiry> findByIdAndGymOwnerId(Long id, Long gymOwnerId);

}
