package com.example.mugbackend.transaction.controller;


import com.example.mugbackend.transaction.dto.MonthlyTransactionDto;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{year}/{month}")
    public MonthlyTransactionDto getMonthlyTransactions(
            @AuthenticationPrincipal User user,
            @PathVariable int year,
            @PathVariable int month) {

        int monthTotal = transactionService.getMonthTotal(user.getId(), year, month);



//        transactionService.getMonthlyDaily(user.getId(), year, month);

//        return transactionService.getMonthlyTransactions(user.getId(), year, month);
    }
}