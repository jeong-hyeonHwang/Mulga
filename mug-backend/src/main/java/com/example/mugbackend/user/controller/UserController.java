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
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.domain.User;
import com.example.mugbackend.user.dto.MainDto;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

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

	@PostMapping("/signup")
	public ResponseEntity<?> signup(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody @Valid UserCreateDto dto
	) {
		return ResponseEntity.ok(userService.signUp(userDetails, dto));
	}

    @GetMapping("/main")
    public ResponseEntity<?> getMainInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		int monthTotal = transactionService.getMonthTotal(userDetails.id(), LocalDate.now().getYear(), LocalDate.now().getMonthValue());
		int remainingBudget = userService.calRemainingBudget(userDetails.id(), monthTotal);
        Transaction lastTransaction = transactionService.getLastTransaction(userDetails.id());

        MainDto dto = MainDto.builder()
                .monthTotal(monthTotal)
                .remainingBudget(remainingBudget)
                .lastTransaction(lastTransaction)
                .build();

		return ResponseEntity.ok(dto);
    }
}
