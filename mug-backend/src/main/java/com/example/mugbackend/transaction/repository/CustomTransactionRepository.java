package com.example.mugbackend.transaction.repository;

import java.util.Optional;

import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.user.dto.CustomUserDetails;

public interface CustomTransactionRepository {
	Optional<Transaction> updateTransaction(CustomUserDetails userDetails, Transaction transaction);
}
