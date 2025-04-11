package com.ilm.mulga.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class TransactionCombineRequestDto (
    val mainTransactionId: String,
    val combiningTransactionIds: List<String>
)