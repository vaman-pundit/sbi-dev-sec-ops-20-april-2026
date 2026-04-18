package com.sbi.lms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Map;

@Data
public class LoginRequest {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;
}
