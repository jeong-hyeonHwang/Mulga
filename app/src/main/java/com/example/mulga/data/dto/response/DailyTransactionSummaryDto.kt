package com.example.mulga.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DailyTransactionSummaryDto(
    val isValid: Boolean,
    val income: Long,
    val expense: Long
)