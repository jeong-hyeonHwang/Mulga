package com.ilm.mulga.presentation.model

import java.time.LocalDate

data class DailyTransactionData(
    var date: LocalDate,
    val transactions: List<TransactionItemData>,
)
