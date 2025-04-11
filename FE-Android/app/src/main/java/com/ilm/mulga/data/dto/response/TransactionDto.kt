package com.ilm.mulga.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: String = "",
    val userId: String = "",
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val isCombined: Boolean = false,
    val title: String = "",
    val cost: Int = 0,
    val category: String = "",
    val memo: String = "",
    val vendor: String = "",
    val time: String = "",
    val paymentMethod: String = "",
    val group: List<TransactionDto> = emptyList()
)