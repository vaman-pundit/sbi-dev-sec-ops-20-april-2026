package com.sbi.lms.config;

import com.sbi.lms.model.*;
import com.sbi.lms.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/**
 * Seeds the H2 in-memory database on startup.
 * Uses BCryptPasswordEncoder so test account passwords are correctly hashed at runtime.
 *
 * Test accounts:
 *   admin@sbi.com   / Admin@123    → MANAGER role (sees full PII)
 *   officer@sbi.com / Officer@123  → OFFICER role (PAN masked, CIBIL/income hidden)
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedDatabase(BranchRepository branches,
                                   LoanProductRepository products,
                                   LoanApplicationRepository applications,
                                   AppUserRepository users,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            // ── Users ─────────────────────────────────────────────────────────
            AppUser manager = new AppUser();
            manager.setEmail("admin@sbi.com");
            manager.setPasswordHash(passwordEncoder.encode("Admin@123"));
            manager.setRole("MANAGER");
            users.save(manager);

            AppUser officer = new AppUser();
            officer.setEmail("officer@sbi.com");
            officer.setPasswordHash(passwordEncoder.encode("Officer@123"));
            officer.setRole("OFFICER");
            users.save(officer);

            // ── Branches ──────────────────────────────────────────────────────
            Branch mumbai = new Branch();
            mumbai.setName("Mumbai Main Branch");
            mumbai.setIfscCode("SBIN0000001");
            mumbai.setCity("Mumbai");
            branches.save(mumbai);

            Branch delhi = new Branch();
            delhi.setName("Delhi Connaught Place");
            delhi.setIfscCode("SBIN0000002");
            delhi.setCity("Delhi");
            branches.save(delhi);

            Branch bengaluru = new Branch();
            bengaluru.setName("Bengaluru MG Road");
            bengaluru.setIfscCode("SBIN0000003");
            bengaluru.setCity("Bengaluru");
            branches.save(bengaluru);

            // ── Loan Products ─────────────────────────────────────────────────
            LoanProduct homeLoan = new LoanProduct();
            homeLoan.setProductName("Home Loan");
            homeLoan.setMinAmount(new BigDecimal("500000"));
            homeLoan.setMaxAmount(new BigDecimal("10000000"));
            homeLoan.setInterestRate(new BigDecimal("8.50"));
            products.save(homeLoan);

            LoanProduct personalLoan = new LoanProduct();
            personalLoan.setProductName("Personal Loan");
            personalLoan.setMinAmount(new BigDecimal("50000"));
            personalLoan.setMaxAmount(new BigDecimal("500000"));
            personalLoan.setInterestRate(new BigDecimal("12.00"));
            products.save(personalLoan);

            LoanProduct carLoan = new LoanProduct();
            carLoan.setProductName("Car Loan");
            carLoan.setMinAmount(new BigDecimal("100000"));
            carLoan.setMaxAmount(new BigDecimal("1500000"));
            carLoan.setInterestRate(new BigDecimal("9.25"));
            products.save(carLoan);

            // ── Loan Applications (PII data — used in access control labs) ────
            LoanApplication app1 = new LoanApplication();
            app1.setApplicantName("Rajesh Kumar");
            app1.setEmail("rajesh@example.com");
            app1.setPanNumber("ABCPK1234E");       // PII — masked for OFFICER
            app1.setMonthlyIncome(new BigDecimal("85000"));   // PII
            app1.setCibilScore(720);                // PII
            app1.setLoanAmountRequested(new BigDecimal("2500000"));
            app1.setStatus(ApplicationStatus.SUBMITTED);
            app1.setBranch(mumbai);
            app1.setLoanProduct(homeLoan);
            applications.save(app1);

            LoanApplication app2 = new LoanApplication();
            app2.setApplicantName("Priya Sharma");
            app2.setEmail("priya@example.com");
            app2.setPanNumber("XYZPS5678F");
            app2.setMonthlyIncome(new BigDecimal("55000"));
            app2.setCibilScore(680);
            app2.setLoanAmountRequested(new BigDecimal("350000"));
            app2.setStatus(ApplicationStatus.UNDER_REVIEW);
            app2.setBranch(delhi);
            app2.setLoanProduct(personalLoan);
            applications.save(app2);

            LoanApplication app3 = new LoanApplication();
            app3.setApplicantName("Amit Singh");
            app3.setEmail("amit@example.com");
            app3.setPanNumber("PQRAM9012G");
            app3.setMonthlyIncome(new BigDecimal("120000"));
            app3.setCibilScore(760);
            app3.setLoanAmountRequested(new BigDecimal("800000"));
            app3.setStatus(ApplicationStatus.APPROVED);
            app3.setBranch(bengaluru);
            app3.setLoanProduct(carLoan);
            applications.save(app3);

            LoanApplication app4 = new LoanApplication();
            app4.setApplicantName("Sunita Patel");
            app4.setEmail("sunita@example.com");
            app4.setPanNumber("LMNSP3456H");
            app4.setMonthlyIncome(new BigDecimal("42000"));
            app4.setCibilScore(640);
            app4.setLoanAmountRequested(new BigDecimal("200000"));
            app4.setStatus(ApplicationStatus.REJECTED);
            app4.setBranch(mumbai);
            app4.setLoanProduct(personalLoan);
            applications.save(app4);

            log.info("LMS seed data loaded — 2 users, 3 branches, 3 products, 4 applications");
        };
    }
}
