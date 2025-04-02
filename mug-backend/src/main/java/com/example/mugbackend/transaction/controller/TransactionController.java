package com.example.mugbackend.transaction.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mugbackend.transaction.dto.TransactionCreateDto;
import com.example.mugbackend.transaction.dto.TransactionUpdateDto;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody @Valid TransactionCreateDto dto
    ) {
        return ResponseEntity.ok(transactionService.createTransaction(userDetails, dto));
    }

    @PatchMapping
    public ResponseEntity<?> updateTransaction(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody @Valid TransactionUpdateDto dto
    ) {
        return ResponseEntity.ok(transactionService.updateTransaction(userDetails, dto));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTransaction(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody List<String> transactionIds
    ) {
        transactionService.deleteTransactions(userDetails, transactionIds);
        return ResponseEntity.ok().build();
    }
}
