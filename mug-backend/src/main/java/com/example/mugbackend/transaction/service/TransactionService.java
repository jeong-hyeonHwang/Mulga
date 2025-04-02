package com.example.mugbackend.transaction.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.analysis.service.AnalysisService;
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.dto.TransactionCreateDto;
import com.example.mugbackend.transaction.dto.TransactionDetailDto;
import com.example.mugbackend.transaction.dto.TransactionUpdateDto;
import com.example.mugbackend.transaction.exception.TransactionAccessDeniedException;
import com.example.mugbackend.transaction.exception.TransactionNoHistoryException;
import com.example.mugbackend.transaction.exception.TransactionNotFoundException;
import com.example.mugbackend.transaction.repository.TransactionRepository;
import com.example.mugbackend.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AnalysisRepository analysisRepository;
    private final TransactionRepository transactionRepository;
    private final MongoTemplate mongoTemplate;
    private final AnalysisService analysisService;

    public int getMonthTotal(String userId, int year, int month) {
        String id = userId + "_" + year + "_" + month;
        return analysisRepository.findById(id)
                .map(Analysis::getMonthTotal)
                .orElse(0);
    }

    public Map<Integer, Analysis.DailyAmount> getDaily(String userId, int year, int month) {
        String id = userId + "_" + year + "_" + month;
        return analysisRepository.findById(id)
                .map(Analysis::getDaily)
                .orElse(Collections.emptyMap());
    }

    public LinkedHashMap<Integer, List<Transaction>> getTransactions(String userId, int year, int month) {

        List<Transaction> thisMonthTransactions = transactionRepository
                .findAllByUserIdAndYearAndMonth(userId, year, month);

        Map<Integer, List<Transaction>> groupByDay = thisMonthTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getDay));

        // 거래 내역이 없는 날은 빈 리스트로 채워준다.
        // 31일이 없는 달에도 31일을 빈 리스트로 채워준다.
        int days = 31;
        LinkedHashMap<Integer, List<Transaction>> transactions = new LinkedHashMap<>();
        for (int d = 1; d <= days; d++) {
            transactions.put(d, groupByDay.getOrDefault(d, new ArrayList<>()));
        }

        return transactions;
    }

    public Transaction getLastTransaction(String userId) {
        return transactionRepository
                .findTopByUserIdOrderByTimeDesc(userId)
                .orElseThrow(() ->
                        new TransactionNoHistoryException());
    }

    @Transactional
    public TransactionDetailDto createTransaction(CustomUserDetails userDetails, TransactionCreateDto dto) {
        Transaction transaction = dto.toEntity();
        transaction.setUserId(userDetails.id());

        analysisService.addTransactionToAnalysis(userDetails, transaction);

        transactionRepository.save(transaction);

        return TransactionDetailDto.of(transaction);
    }

    @Transactional
    public TransactionDetailDto updateTransaction(CustomUserDetails userDetails, TransactionUpdateDto dto) {
        Transaction transaction = findById(userDetails, dto.id());

        Transaction updatedTransaction = transactionRepository.updateTransaction(userDetails, dto.toEntity())
            .orElseThrow(TransactionNotFoundException::new);

        analysisService.alterTransactionToAnalysis(userDetails, transaction, updatedTransaction);

        return TransactionDetailDto.of(updatedTransaction);
    }

    @Transactional
    public void deleteTransactions(CustomUserDetails userDetails, List<String> transactionIds) {
        for(String transactionId : transactionIds) {
            Transaction transaction = findById(userDetails, transactionId);
            analysisService.removeTransactionFromAnalysis(userDetails, transaction);
        }

        transactionRepository.deleteAllByIdIn(transactionIds);
    }

    private Transaction findById(CustomUserDetails userDetails, String id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException());

        if(!userDetails.id().equals(transaction.getUserId())) {
            throw new TransactionAccessDeniedException();
        }
        return transaction;
    }

}
