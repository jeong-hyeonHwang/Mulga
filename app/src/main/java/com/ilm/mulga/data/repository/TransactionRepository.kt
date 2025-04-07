package com.ilm.mulga.data.repository

import com.ilm.mulga.data.service.TransactionService
import com.ilm.mulga.domain.mapper.toDomain
import com.ilm.mulga.domain.model.MonthlyTransactionEntity

class TransactionRepository(private val transactionService: TransactionService) {

    suspend fun getMonthlyData(year: Int, month: Int): MonthlyTransactionEntity? {
        val response = transactionService.getMonthlyTransactions(year, month)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }
}
