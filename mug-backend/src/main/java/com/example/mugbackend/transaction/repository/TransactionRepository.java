package com.example.mugbackend.transaction.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mugbackend.transaction.domain.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByUserIdAndYearAndMonth(String userId, int year, int month);
    void deleteAllByIdIn(List<String> ids);
}
