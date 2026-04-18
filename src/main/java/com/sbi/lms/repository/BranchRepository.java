package com.sbi.lms.repository;

import com.sbi.lms.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByIfscCode(String ifscCode);
    boolean existsByIfscCode(String ifscCode);
}
