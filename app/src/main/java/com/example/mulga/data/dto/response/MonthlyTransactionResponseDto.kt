package com.example.mulga.data.dto.response

import kotlinx.serialization.Serializable


@Serializable
data class MonthlyTransactionResponseDto(
    val monthTotal: Long,
    val year: Int,
    val month: Int,
    val daily: Map<String, DailyTransactionSummaryDto>,
    val transactions: Map<String, List<TransactionDto>>
)