package com.example.mulga.domain.model

data class TransactionEntity(
    val id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val isCombined: Boolean,
    val title: String,
    val cost: Long,
    val category: String,
    val memo: String?,
    val vendor: String?,
    val time: String, // 필요에 따라 Date 타입으로 변환
    val paymentMethod: String,
    val group: List<TransactionEntity>
)
