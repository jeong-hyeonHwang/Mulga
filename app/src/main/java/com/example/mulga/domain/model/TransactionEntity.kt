package com.example.mulga.domain.model

import java.time.LocalDateTime

data class TransactionEntity(
    val id: String,
    val userId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val isCombined: Boolean,
    val title: String,
    val cost: Int,
    val category: String,
    val memo: String,
    val vendor: String,
    val time: LocalDateTime,
    val paymentMethod: String,
    val group: List<TransactionEntity>
)
