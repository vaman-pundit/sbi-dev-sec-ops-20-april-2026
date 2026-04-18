package com.sbi.lms.controller;

import com.sbi.lms.model.LoanProduct;
import com.sbi.lms.repository.LoanProductRepository;
import com.sbi.lms.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Loan Products")
@SecurityRequirement(name = "bearerAuth")
public class LoanProductController {

    private final LoanProductRepository productRepository;

    public LoanProductController(LoanProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','OFFICER')")
    @Operation(summary = "List all loan products")
    public ResponseEntity<List<LoanProduct>> listAll() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','OFFICER')")
    @Operation(summary = "Get loan product by ID")
    public ResponseEntity<LoanProduct> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id)));
    }
}
