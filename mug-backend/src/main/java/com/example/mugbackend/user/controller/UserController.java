package com.example.mugbackend.user.controller;

import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.domain.User;
import com.example.mugbackend.user.dto.MainDto;
import com.example.mugbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping
    public MainDto getMainInfo(User user) {
        int monthTotal = transactionService.getMonthTotal(user.getId(), LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        int remainingBudget = userService.getRemainingBudget(user.getId());
        Transaction lastTransaction = transactionService.getLastTransaction(user.getId());

        return MainDto.builder()
                .monthTotal(monthTotal)
                .remainingBudget(remainingBudget)
                .lastTransaction(lastTransaction)
                .build();
    }
}
