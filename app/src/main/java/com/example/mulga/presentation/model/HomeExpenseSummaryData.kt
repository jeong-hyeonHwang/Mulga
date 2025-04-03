package com.example.mulga.presentation.model

import com.example.mulga.domain.model.HomeExpenseSummaryEntity
import com.example.mulga.presentation.mapper.toTransactionItemDataForMain
import java.text.NumberFormat
import java.util.Locale

data class HomeExpenseSummaryData(
    val monthTotal: String = "---",
    val remainingBudget: String = "---",
    val lastTransaction: TransactionItemData? = null,
    var baselineFraction: Float = 0f
)

fun HomeExpenseSummaryEntity.toPresentation(): HomeExpenseSummaryData {
    val totalBudget = monthTotal + remainingBudget
    val baselineFraction = if (remainingBudget == 0) {
        1.0f
    } else {
        monthTotal.toFloat() / totalBudget.toFloat()
    }
    val formattedMonthTotal = NumberFormat.getNumberInstance(Locale.getDefault()).format(monthTotal)
    val formattedRemainingBudget = NumberFormat.getNumberInstance(Locale.getDefault()).format(remainingBudget)
    return HomeExpenseSummaryData(
        monthTotal = formattedMonthTotal,
        remainingBudget = formattedRemainingBudget,
        lastTransaction = lastTransaction.toTransactionItemDataForMain(),
        baselineFraction = baselineFraction
    )
}
