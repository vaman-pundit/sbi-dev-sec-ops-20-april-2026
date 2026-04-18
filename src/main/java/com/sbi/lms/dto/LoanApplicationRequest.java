package com.sbi.lms.dto;

import com.sbi.lms.model.ApplicationStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

// ── Request DTO — used for POST /api/v1/applications ──────────────────────────
// DevSecOps: all fields validated with Bean Validation (A03 Injection, A01 Broken Access Control)
@Data
public class LoanApplicationRequest {

    @NotBlank(message = "Applicant name is required")
    @Size(min = 2, max = 100, message = "Applicant name must be 2-100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Name must contain only letters, spaces, or hyphens")
    private String applicantName;

    @NotBlank
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Must be a valid PAN number format")
    private String panNumber;

    @NotNull
    @Positive(message = "Monthly income must be a positive value")
    private BigDecimal monthlyIncome;

    @NotNull
    @Positive(message = "Requested loan amount must be positive")
    private BigDecimal loanAmountRequested;

    @NotNull
    @Min(value = 300, message = "CIBIL score cannot be below 300")
    @Max(value = 900, message = "CIBIL score cannot exceed 900")
    private Integer cibilScore;

    @NotNull
    private Long branchId;

    @NotNull
    private Long loanProductId;
}
