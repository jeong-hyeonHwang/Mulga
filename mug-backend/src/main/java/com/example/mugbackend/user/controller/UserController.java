package com.example.mugbackend.user.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mugbackend.user.dto.CustomUserDetails;
import com.example.mugbackend.user.dto.UserCreateDto;
import com.example.mugbackend.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(userDetails.toEntity());
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid UserCreateDto dto
	) {
		return ResponseEntity.ok(userService.signUp(userDetails, dto));
	}
}
