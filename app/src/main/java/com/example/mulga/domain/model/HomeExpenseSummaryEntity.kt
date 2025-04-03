package com.example.mulga.domain.model

import com.example.mulga.data.dto.response.HomeExpenseSummaryResponseDto
import com.example.mulga.domain.mapper.toDomain

data class HomeExpenseSummaryEntity (
    val monthTotal: Int,
    val remainingBudget: Int,
    val lastTransaction: TransactionEntity
)

fun HomeExpenseSummaryResponseDto.toDomain(): HomeExpenseSummaryEntity {
    return HomeExpenseSummaryEntity(
        monthTotal = monthTotal,
        remainingBudget = remainingBudget,
        lastTransaction = lastTransaction.toDomain()
    )
}