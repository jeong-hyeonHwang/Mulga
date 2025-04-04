package com.example.mugbackend.transaction.repository;

import java.util.Optional;
import java.util.List;

import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.user.dto.CustomUserDetails;

public interface CustomTransactionRepository {
	Optional<Transaction> updateTransaction(CustomUserDetails userDetails, Transaction transaction);

	void updateGroupAndCostById(String transactionId, List<Transaction> newGroup, Integer newCost);
}
