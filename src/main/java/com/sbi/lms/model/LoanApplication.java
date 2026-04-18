package com.sbi.lms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter @Setter
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private String email;

    // PII — masked in toString; role-based access in controller
    @Column(nullable = false)
    private String panNumber;

    // PII — masked for OFFICER role
    @Column(nullable = false)
    private BigDecimal monthlyIncome;

    // PII — masked for OFFICER role
    @Column(nullable = false)
    private Integer cibilScore;

    @Column(nullable = false)
    private BigDecimal loanAmountRequested;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_product_id")
    private LoanProduct loanProduct;

    // DevSecOps: Never log PII — CIBIL, PAN, income are always [REDACTED]
    @Override
    public String toString() {
        return "LoanApplication{id=" + id +
               ", pan=[REDACTED], cibilScore=[REDACTED], income=[REDACTED]}";
    }
}
