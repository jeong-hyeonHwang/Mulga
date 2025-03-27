package com.example.mulga.model

data class TransactionDayModel(
    var year: Int,
    var month: Int,
    var day: Int,
    val transactions: List<TransactionItemModel>
)
