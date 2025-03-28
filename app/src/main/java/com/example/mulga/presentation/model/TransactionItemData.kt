package com.example.mulga.presentation.model

import com.example.mulga.presentation.model.type.Category

data class TransactionItemData(
    val category: Category?,
    val title: String,
    val subtitle: String,
    val price: String,
    val time: String
)
