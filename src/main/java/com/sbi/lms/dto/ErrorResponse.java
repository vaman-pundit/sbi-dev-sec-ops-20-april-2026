package com.sbi.lms.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ErrorResponse {
    private String message;
    private Map<String, String> fieldErrors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, Map<String, String> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
}
