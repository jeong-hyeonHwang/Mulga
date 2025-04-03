package com.example.mulga.data.repository

import com.example.mulga.data.service.TransactionService
import com.example.mulga.domain.mapper.toDomain
import com.example.mulga.domain.model.MonthlyTransactionEntity

class TransactionRepository(private val transactionService: TransactionService) {

    suspend fun getMonthlyData(year: Int, month: Int): MonthlyTransactionEntity? {
        val response = transactionService.getMonthlyTransactions(year, month)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }
}
