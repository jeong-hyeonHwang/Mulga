package com.example.mulga.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val _id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val isCombined: Boolean,
    val title: String,
    val cost: Long,
    val category: String,
    val memo: String?,
    val vendor: String?,
    val time: String,
    val paymentMethod: String,
    val bank: String? = null,
    val group: List<TransactionDto>
)
