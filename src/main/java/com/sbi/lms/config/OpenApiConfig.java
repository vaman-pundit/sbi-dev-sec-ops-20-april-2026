package com.sbi.lms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration — exposes /v3/api-docs for ZAP DAST auto-discovery.
 * The Authorize button in Swagger UI lets you paste a JWT token for manual testing.
 */
@Configuration
@OpenAPIDefinition(info = @Info(
        title       = "SBI Loan Management System API",
        version     = "1.0",
        description = "DevSecOps Training — SBI LMS REST API. " +
                      "Login via POST /api/v1/auth/login, then click Authorize."
))
@SecurityScheme(
        name   = "bearerAuth",
        type   = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
