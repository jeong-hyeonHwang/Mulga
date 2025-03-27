package com.example.mugbackend.user.service;

import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.repository.UserRepository;
import com.example.mugbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AnalysisRepository analysisRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;


    public int getRemainingBudget(String userId) {
        int budget = getBudget(userId);
        int monthTotal =transactionService.getMonthTotal(userId,
                LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        int remainingBudget = budget - monthTotal;
        if(remainingBudget > 0) {
            return remainingBudget;
        }
        return 0;
    }

    public int getBudget(String userId) {
        return userRepository.findById(userId)
                .map(User::getBudget)
                .filter(Objects::nonNull)
                .orElse(0);
    }

}
