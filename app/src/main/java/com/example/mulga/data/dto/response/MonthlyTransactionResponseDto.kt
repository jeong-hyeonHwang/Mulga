package com.example.mulga.data.dto.response

import kotlinx.serialization.Serializable


@Serializable
data class MonthlyTransactionResponseDto(
    val monthTotal: Int = 0,
    val year: Int = 0,
    val month: Int = 0,
    val daily: Map<String, DailyTransactionSummaryDto> = emptyMap(),
    val transactions: Map<String, List<TransactionDto>> = emptyMap()
)