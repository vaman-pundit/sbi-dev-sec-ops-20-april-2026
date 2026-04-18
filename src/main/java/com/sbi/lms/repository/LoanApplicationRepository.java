package com.sbi.lms.repository;

import com.sbi.lms.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    // DevSecOps: Spring Data derived query — safe by design (no SQL string building)
    // Used in the capstone lab FIX to replace the vulnerable string-concat JPQL
    List<LoanApplication> findByBranchName(String branchName);
}
