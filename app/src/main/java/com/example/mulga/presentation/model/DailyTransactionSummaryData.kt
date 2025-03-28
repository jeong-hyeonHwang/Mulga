package com.example.mulga.presentation.model

data class DailyTransactionSummaryData(
    var day: Int,
    var isValid: Boolean,
    var income: Int,
    var expense: Int
)