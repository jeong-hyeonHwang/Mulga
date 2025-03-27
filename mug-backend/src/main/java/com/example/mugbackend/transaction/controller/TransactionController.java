package com.example.mugbackend.transaction.controller;


import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.dto.MonthlyTransactionDto;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        Map<Integer, Analysis.DailyAmount> daily = transactionService.getDaily(user.getId(), year, month);
        LinkedHashMap<Integer, List<Transaction>> transactions = transactionService.getTransactions(user.getId(), year, month);

        return MonthlyTransactionDto.builder()
                .monthTotal(monthTotal)
                .year(year)
                .month(month)
                .daily(daily)
                .transactions(transactions)
                .build();
    }
}