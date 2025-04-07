package com.ilm.mulga.presentation.model

import java.time.LocalDate

data class MonthlyTotalTransactionData (
    var year: Int = LocalDate.now().year,
    var month: Int = LocalDate.now().monthValue,
    var monthTotal: Int = 0
)