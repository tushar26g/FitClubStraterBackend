package com.example.gym_saas_backend.repository;

import com.example.gym_saas_backend.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmailOrMobileNumber(String email, String mobileNumber);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
}

