package com.ilm.mulga.domain.model

data class DailyTransactionSummaryEntity(
    val isValid: Boolean,
    val income: Long,
    val expense: Long
)