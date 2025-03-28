package com.example.mugbackend.transaction.repository;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByUserIdAndYearAndMonth(String userId, int year, int month);
}