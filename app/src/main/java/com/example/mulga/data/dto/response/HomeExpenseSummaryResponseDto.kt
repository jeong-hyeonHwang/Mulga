package com.example.mulga.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeExpenseSummaryResponseDto (
    val monthTotal: Int = 0,
    val remainingBudget: Int = 0,
    val lastTransaction: TransactionDto = TransactionDto()
)