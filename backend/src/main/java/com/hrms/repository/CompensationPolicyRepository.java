package com.hrms.repository;

import com.hrms.entity.CompensationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Compensation Policy Repository
 */
@Repository
public interface CompensationPolicyRepository extends JpaRepository<CompensationPolicy, UUID> {
    Optional<CompensationPolicy> findByName(String name);

    Optional<CompensationPolicy> findFirstByActiveTrue();
}

