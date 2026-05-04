package com.hrms.repository;

import com.hrms.entity.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Timesheet Repository
 */
@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, UUID> {
    Optional<Timesheet> findByEmployeeIdAndTimesheetDate(UUID employeeId, LocalDate date);

    Page<Timesheet> findByEmployeeId(UUID employeeId, Pageable pageable);

    Page<Timesheet> findByStatus(String status, Pageable pageable);

    @Query("SELECT t FROM Timesheet t WHERE t.employee.manager.id = :managerId AND t.status = 'SUBMITTED' ORDER BY t.submittedAt DESC")
    Page<Timesheet> findPendingApprovalsByManager(@Param("managerId") UUID managerId, Pageable pageable);

    @Query("SELECT t FROM Timesheet t WHERE t.employee.id = :employeeId AND t.timesheetDate BETWEEN :fromDate AND :toDate")
    Page<Timesheet> findByEmployeeAndDateRange(@Param("employeeId") UUID employeeId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}

