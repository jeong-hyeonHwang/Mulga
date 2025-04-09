package com.ilm.mulga.presentation.model

import com.ilm.mulga.presentation.model.type.Category
import kotlinx.serialization.Serializable

@Serializable
data class TransactionItemData(
    val id: String,
    val category: Category?,
    val title: String,
    val subtitle: String,
    val price: String,
    val time: String,
    val isCombined: Boolean = false
)
