package com.example.mugbackend.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.mugbackend.transaction.domain.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String>, CustomTransactionRepository {
    List<Transaction> findAllByUserIdAndYearAndMonth(String userId, int year, int month);
    Optional<Transaction> findTopByUserIdOrderByTimeDesc(String userId);
    void deleteAllByIdIn(List<String> ids);
}
