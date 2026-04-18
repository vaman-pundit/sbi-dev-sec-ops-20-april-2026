package com.sbi.lms.dto;

import com.sbi.lms.model.ApplicationStatus;
import lombok.Data;
import java.math.BigDecimal;

// Response DTO — PII fields (cibilScore, monthlyIncome, panNumber) are nullable
// so the controller can null them out for OFFICER role (role-based field masking)
@Data
public class LoanApplicationDTO {

    private Long id;
    private String applicantName;
    private String email;

    // PII — masked to last 4 chars for OFFICER; full value for MANAGER only
    private String panNumber;

    // PII — nulled for OFFICER role
    private BigDecimal monthlyIncome;

    // PII — nulled for OFFICER role
    private Integer cibilScore;

    private BigDecimal loanAmountRequested;
    private ApplicationStatus status;
    private String branchName;
    private String productName;
}
