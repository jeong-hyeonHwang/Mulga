package com.ilm.mulga.presentation.model

import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.util.serialization.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TransactionDetailData (
    val id: String,
    val title: String,
    val category: Category?,
    val vendor: String,
    @Serializable(with = LocalDateTimeSerializer::class) val time: LocalDateTime,
    var cost: Int,
    val memo: String,
    val paymentMethod: String,
    val group: List<TransactionDetailData> = emptyList()
)