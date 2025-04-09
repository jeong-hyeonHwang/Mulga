package com.ilm.mulga.data.repository

import com.ilm.mulga.data.dto.request.TransactionCombineRequestDto
import com.ilm.mulga.data.service.TransactionService
import com.ilm.mulga.domain.mapper.toDomain
import com.ilm.mulga.domain.model.MonthlyTransactionEntity

class TransactionRepository(private val transactionService: TransactionService) {

    suspend fun getMonthlyData(year: Int, month: Int): MonthlyTransactionEntity? {
        val response = transactionService.getMonthlyTransactions(year, month)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    suspend fun deleteTransactions(deletedIds: LinkedHashSet<String>): Boolean {
        val response = transactionService.deleteTransactions(deletedIds.toList())
        return response.isSuccessful
    }

    suspend fun combineTransactions(mainTransactionId: String, combineTransactionIds: LinkedHashSet<String>): MonthlyTransactionEntity? {
        val response = transactionService.combineTransaction(
            TransactionCombineRequestDto(mainTransactionId, combineTransactionIds.toList()))
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }
}
