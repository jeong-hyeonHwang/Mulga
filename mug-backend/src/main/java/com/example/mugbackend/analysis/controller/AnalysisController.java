package com.example.mugbackend.analysis.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mugbackend.analysis.service.AnalysisService;
import com.example.mugbackend.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {
	private final AnalysisService analysisService;

	@GetMapping("/{year}/{month}")
	public ResponseEntity<?> getAnalysis(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable @Min(2000) @Max(2100) Integer year,
		@PathVariable @Min(1) @Max(12) Integer month
	) {
		return ResponseEntity.ok(analysisService.getAnalysisDetail(userDetails, year, month));
	}
}
