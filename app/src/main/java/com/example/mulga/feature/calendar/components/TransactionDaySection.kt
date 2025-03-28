package com.example.mulga.feature.calendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mulga.R
import com.example.mulga.presentation.model.DailyTransactionData

@Composable
fun TransactionDaySection(
    dailyTransactionData: DailyTransactionData
) {
    Column {
        // 날짜 헤더
        TransactionDayHeader(
            dateLabel = stringResource(
                id = R.string.calendar_month_day,
                dailyTransactionData.month,
                dailyTransactionData.day
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        // 거래 내역 리스트
        dailyTransactionData.transactions.forEach { item ->
            TransactionItem(item = item)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
