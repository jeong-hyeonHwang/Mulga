package com.example.mulga.domain.model

data class MonthlyTransactionEntity(
    val monthTotal: Long,
    val year: Int,
    val month: Int,
    val dailySummaries: Map<Int, DailyTransactionSummaryEntity>,
    val transactions: Map<Int, List<TransactionEntity>>
)