package com.example.mugbackend.transaction.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.analysis.service.AnalysisService;
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.dto.MonthlyTransactionDto;
import com.example.mugbackend.transaction.dto.TransactionCombineDto;
import com.example.mugbackend.transaction.dto.TransactionCreateDto;
import com.example.mugbackend.transaction.dto.TransactionDetailDto;
import com.example.mugbackend.transaction.dto.TransactionUpdateDto;
import com.example.mugbackend.transaction.exception.TransactionAccessDeniedException;
import com.example.mugbackend.transaction.exception.TransactionCombineConflictException;
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

    public MonthlyTransactionDto buildMonthlyTransactionDto(CustomUserDetails userDetails, int year, int month) {

        String id = userDetails.id();

        int monthTotal = getMonthTotal(id, year, month);
        Map<Integer, Analysis.DailyAmount> daily = getDaily(id, year, month);
        LinkedHashMap<Integer, List<TransactionDetailDto>> transactions = getTransactionsByTimeDESC(id, year, month);

        MonthlyTransactionDto dto = MonthlyTransactionDto.builder()
                .monthTotal(monthTotal)
                .year(year)
                .month(month)
                .daily(daily)
                .transactions(transactions)
                .build();

        return dto;
    }

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

    public LinkedHashMap<Integer, List<TransactionDetailDto>> getTransactionsByTimeDESC(String userId, int year, int month) {

        List<Transaction> thisMonthTransactions = transactionRepository
                .findAllByUserIdAndYearAndMonth(userId, year, month);

        Map<Integer, List<TransactionDetailDto>> groupByDaySortByTimeDesc = thisMonthTransactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::getDay,
                Collectors.mapping(
                    TransactionDetailDto::of,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.stream()
                            .sorted(Comparator.comparing(TransactionDetailDto::time).reversed())
                            .collect(Collectors.toList())
                    )
                )
            ));

        // 거래 내역이 없는 날은 빈 리스트로 채워준다.
        // 31일이 없는 달에도 31일을 빈 리스트로 채워준다.
        int days = 31;
        LinkedHashMap<Integer, List<TransactionDetailDto>> transactions = new LinkedHashMap<>();
        for (int d = 1; d <= days; d++) {
            transactions.put(d, groupByDaySortByTimeDesc.getOrDefault(d, new ArrayList<>()));
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
            if(transaction.getIsCombined()) {
                analysisService.deleteCombinedTransactionfromAnalysis(userDetails, transaction);
            }
            else {
                analysisService.removeTransactionFromAnalysis(userDetails, transaction);
            }
        }

        transactionRepository.deleteAllByIdIn(transactionIds);
    }

    @Transactional
	public TransactionDetailDto combineTransactions(CustomUserDetails userDetails, TransactionCombineDto dto) {
        Transaction mainTransaction = findById(userDetails, dto.mainTransactionId());
        List<Transaction> combiningTransactions = findByIds(userDetails, dto.combiningTransactionIds());

        if(!mainTransaction.getGroup().isEmpty()) {
            throw new TransactionCombineConflictException();
        }

        Transaction newTransaction = Transaction.builder()
            .userId(mainTransaction.getUserId())
            .year(mainTransaction.getYear())
            .month(mainTransaction.getMonth())
            .day(mainTransaction.getDay())
            .isCombined(true)
            .title(mainTransaction.getTitle())
            .cost(mainTransaction.getCost())
            .category(mainTransaction.getCategory())
            .memo(mainTransaction.getMemo())
            .vendor(mainTransaction.getVendor())
            .time(mainTransaction.getTime())
            .paymentMethod(mainTransaction.getPaymentMethod())
            .group(new ArrayList<>())
            .build();

        List<Transaction> newGroup = newTransaction.getGroup();
        int newCost = 0;

        combiningTransactions.add(mainTransaction);
        for(Transaction transaction : combiningTransactions) {
            newGroup.add(transaction);
            newCost += transaction.getCost();
        }

        newTransaction.setCost(newCost);
        newTransaction.setGroup(
            newGroup.stream()
                .sorted(Comparator.comparing(Transaction::getTime))
                .collect(Collectors.toList())
        );

        List<String> combinedTransactionIds = dto.combiningTransactionIds();
        combinedTransactionIds.add(mainTransaction.getId());

        transactionRepository.save(newTransaction);
        transactionRepository.deleteAllByIdIn(combinedTransactionIds);

        analysisService.addCombinedTransactiontoAnalysis(userDetails, newTransaction);

        return TransactionDetailDto.of(newTransaction);
    }

    @Transactional
    public List<TransactionDetailDto> uncombineTransactions(CustomUserDetails userDetails, String transactionId) {
        Transaction mainTransaction = findById(userDetails, transactionId);
        List<TransactionDetailDto> transactionDetailDtos = new ArrayList<>();

        for(Transaction transaction: mainTransaction.getGroup()) {
            transactionRepository.save(transaction);
            transactionDetailDtos.add(TransactionDetailDto.of(transaction));

        }

        analysisService.removeCombinedTransactionfromAnalysis(userDetails, mainTransaction);

        transactionRepository.deleteById(transactionId);


        return transactionDetailDtos;
    }

    private Transaction findById(CustomUserDetails userDetails, String id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException());

        if(!userDetails.id().equals(transaction.getUserId())) {
            throw new TransactionAccessDeniedException();
        }
        return transaction;
    }

    private List<Transaction> findByIds(CustomUserDetails userDetails, List<String> ids) {
        List<Transaction> transactions = transactionRepository.findAllById(ids);

        if(transactions.size() != ids.size()) {
            throw new TransactionNotFoundException();
        }

        for(Transaction transaction : transactions) {
            if(!userDetails.id().equals(transaction.getUserId())) {
                throw new TransactionAccessDeniedException();
            }
        }

        return transactions;
    }
}
