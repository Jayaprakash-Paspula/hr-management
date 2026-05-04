package com.hrms.repository;

import com.hrms.entity.Payslip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Payslip Repository
 */
@Repository
public interface PayslipRepository extends JpaRepository<Payslip, UUID> {
    Optional<Payslip> findByEmployeeIdAndPayrollMonth(UUID employeeId, LocalDate payrollMonth);

    Page<Payslip> findByEmployeeId(UUID employeeId, Pageable pageable);

    Page<Payslip> findByPayrollMonth(LocalDate payrollMonth, Pageable pageable);
}

