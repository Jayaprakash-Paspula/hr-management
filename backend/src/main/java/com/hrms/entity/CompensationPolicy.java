package com.hrms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Compensation Policy entity
 */
@Entity
@Table(name = "compensation_policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompensationPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal overtimeMultiplier; // Default 1.5x

    @Column(nullable = false)
    private BigDecimal maxHoursPerDay; // Default 8

    @Column(nullable = false)
    private BigDecimal maxHoursPerWeek; // Default 40

    @Column
    private BigDecimal bonusPercentage; // Annual bonus as % of base salary

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

