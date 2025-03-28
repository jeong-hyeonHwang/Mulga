package com.example.mulga.presentation.model

data class DailyTransactionData(
    var month: Int,
    var day: Int,
    val transactions: List<TransactionItemData>
)
