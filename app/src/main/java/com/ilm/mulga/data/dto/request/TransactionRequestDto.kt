package com.ilm.mulga.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequestDto(
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val cost: Int,
    val category: String,
    val time: String,
    val vendor: String,
    val paymentMethod: String,
    val memo: String
)
