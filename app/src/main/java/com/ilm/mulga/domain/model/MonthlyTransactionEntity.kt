package com.ilm.mulga.domain.model

data class MonthlyTransactionEntity(
    val monthTotal: Int,
    val year: Int,
    val month: Int,
    val dailySummaries: Map<Int, DailyTransactionSummaryEntity>,
    val transactions: Map<Int, List<TransactionEntity>>
)