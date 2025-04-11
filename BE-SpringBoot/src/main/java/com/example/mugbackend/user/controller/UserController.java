package com.example.mugbackend.user.controller;

import java.time.LocalDate;

import com.example.mugbackend.user.dto.UserUpdateDto;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.dto.TransactionDetailDto;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.dto.CustomUserDetails;
import com.example.mugbackend.user.dto.MainDto;
import com.example.mugbackend.user.dto.UserCreateDto;
import com.example.mugbackend.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
	private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/me")
	public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(userDetails.toEntity());
	}

	@PatchMapping("/me")
	public ResponseEntity<?> update(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody @Valid UserUpdateDto updateDto
	) {
		return ResponseEntity.ok(userService.updateUser(userDetails, updateDto));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody @Valid UserCreateDto dto
	) {
		return ResponseEntity.ok(userService.signUp(userDetails, dto));
	}

    @GetMapping("/main")
    public ResponseEntity<?> getMainInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		String userId = userDetails.id();
		int monthTotal = transactionService.getMonthTotal(userId, LocalDate.now().getYear(), LocalDate.now().getMonthValue());
		int remainingBudget = userService.calRemainingBudget(userId, monthTotal);

        Transaction lastTransaction = transactionService.getLastTransaction(userId);
        MainDto dto = MainDto.builder()
                .monthTotal(monthTotal)
                .remainingBudget(remainingBudget)
                .lastTransaction(TransactionDetailDto.of(lastTransaction))
                .build();

		return ResponseEntity.ok(dto);
    }
}
