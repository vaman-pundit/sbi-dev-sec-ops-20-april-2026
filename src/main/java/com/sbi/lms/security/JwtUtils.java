package com.sbi.lms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * JWT utility for token generation and validation.
 *
 * ╔══════════════════════════════════════════════════════════╗
 * ║                                                          ║
 * ║  LAB 1 — SAST EXERCISE                                   ║
 * ║                                                          ║
 * ║  SonarQube will flag JWT_SECRET below as a               ║
 * ║                                                          ║
 * ║  CRITICAL vulnerability (rule: java:S6418).              ║
 * ║                                                          ║
 * ║  Your task: replace the hardcoded string with            ║
 * ║                                                          ║
 * ║  the @Value("${jwt.secret}") injection below it,         ║
 * ║                                                          ║
 * ║  then re-run the scan and confirm the gate passes.       ║
 * ║                                                          ║
 * ╚══════════════════════════════════════════════════════════╝
 * 
 */

@Component
public class JwtUtils {

	private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

	// ── VULNERABLE — SonarQube rule java:S6418 will flag this ─────────────────
	// LAB 1 FIX: delete the two lines below and uncomment the @Value field
	private static final String JWT_SECRET = "SBIBankingSecretKey2024SBIBankingSecretKey2024";
	private static final long JWT_EXPIRY = 86400000; // 24 hours — too long for banking!
	// ──────────────────────────────────────────────────────────────────────────

	// LAB 1 FIX: uncomment these two lines after deleting the hardcoded values
	// above
	// @Value("${jwt.secret}")
	// private String jwtSecret;
	//
	// @Value("${jwt.expiration.ms:900000}") // 15 minutes — correct for banking
	// private long jwtExpirationMs;

	public String generateToken(String email, String role) {
		Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
		return Jwts.builder().setSubject(email).claim("role", role).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRY))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String getEmailFromToken(String token) {
		return parseClaims(token).getSubject();
	}

	public String getRoleFromToken(String token) {
		return (String) parseClaims(token).get("role");
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.warn("Invalid JWT token: {}", e.getMessage());
			return false;
		}
	}

	private Claims parseClaims(String token) {
		Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
