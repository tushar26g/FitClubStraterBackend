package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByGymOwnerIdOrderByMembershipEndDateAsc(Long gymOwnerId);

    @Query("SELECT m FROM Member m WHERE m.gymOwnerId = :gymOwnerId AND (m.mobileNumber = :mobileNumber OR m.email = :email)")
    Optional<Member> findByGymOwnerIdAndMobileNumberOrEmail(Long gymOwnerId, String mobileNumber, String email);

    @Query("SELECT m FROM Member m WHERE m.gymOwnerId = :gymOwnerId AND " +
            "(LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY m.membershipEndDate ASC")
    List<Member> searchByGymOwnerIdAndKeyword(Long gymOwnerId, String search);

    Optional<Member> findByIdAndGymOwnerId(Long id, Long gymOwnerId);

    List<Member> findByGymOwnerIdAndMembershipStatusOrderByMembershipEndDateAsc(Long gymOwnerId, Member.MembershipStatus status);

    @Query("SELECT m FROM Member m WHERE m.gymOwnerId = :gymOwnerId AND m.membershipStatus = :status AND " +
            "(LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "ORDER BY m.membershipEndDate ASC")
    List<Member> searchByGymOwnerIdAndKeywordAndStatus(Long gymOwnerId, String search, Member.MembershipStatus status);

    List<Member> findByGymOwnerId(Long gymOwnerId);
}

