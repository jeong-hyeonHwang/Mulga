package com.example.mulga.model

import com.example.mulga.model.enums.Category

data class TransactionItemModel(
    val category: Category?,
    val title: String,
    val subtitle: String,
    val price: String,
    val time: String
)
