package com.hrms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Payslip entity
 */
@Entity
@Table(name = "payslips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate payrollMonth;

    @Column(nullable = false)
    private BigDecimal basePay;

    @Column(nullable = false)
    private BigDecimal overtimePay;

    @Column(nullable = false)
    private BigDecimal bonus;

    @Column(nullable = false)
    private BigDecimal totalEarnings;

    @Column(nullable = false)
    private BigDecimal taxDeduction;

    @Column(nullable = false)
    private BigDecimal otherDeductions;

    @Column(nullable = false)
    private BigDecimal totalDeductions;

    @Column(nullable = false)
    private BigDecimal netPay;

    @Column
    private String pdfPath;

    @OneToMany(mappedBy = "payslip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayslipDetail> details = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

