package com.hrms.controller;

import com.hrms.dto.CompensationCalculatorRequest;
import com.hrms.dto.CompensationCalculatorResponse;
import com.hrms.service.CompensationCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Compensation Calculator Controller
 * Main feature for calculating employee pay with real-time preview
 */
@RestController
@RequestMapping("/compensation-calculator")
@Tag(name = "Compensation Calculator", description = "CRC - Calculate employee compensation")
@Slf4j
public class CompensationCalculatorController {

    @Autowired
    private CompensationCalculatorService compensationCalculatorService;

    /**
     * Calculate compensation with real-time preview
     * Interactive endpoint for the CRC calculator UI
     */
    @PostMapping("/calculate")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(
        summary = "Calculate compensation",
        description = "Calculate employee pay based on hourly rate, overtime, bonuses, and deductions. Returns detailed breakdown for UI display."
    )
    public ResponseEntity<CompensationCalculatorResponse> calculateCompensation(
            @Valid @RequestBody CompensationCalculatorRequest request) {
        log.info("Compensation calculation request received");
        CompensationCalculatorResponse response = compensationCalculatorService.calculateCompensation(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Preview calculation (same as calculate, but could be rate-limited or cached)
     */
    @PostMapping("/preview")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @Operation(summary = "Preview compensation", description = "Real-time preview of compensation calculation")
    public ResponseEntity<CompensationCalculatorResponse> previewCompensation(
            @Valid @RequestBody CompensationCalculatorRequest request) {
        log.info("Compensation preview request received");
        CompensationCalculatorResponse response = compensationCalculatorService.calculateCompensation(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "CRC Health check", description = "Compensation calculator service health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Compensation Calculator is healthy");
    }
}

