package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByIdAndGymOwnerId(Long id, Long gymOwnerId);
}
