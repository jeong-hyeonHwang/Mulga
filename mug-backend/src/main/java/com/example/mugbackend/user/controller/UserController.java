package com.example.mugbackend.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mugbackend.user.dto.CustomUserDetails;

@RestController
public class UserController {
	@GetMapping("/me")
	public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.status(HttpStatus.OK).body(userDetails.toEntity());
	}
}
