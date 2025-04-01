package com.example.mulga.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.feature.calendar.components.CalendarHeaderView
import com.example.mulga.feature.calendar.components.CustomCalendarView
import com.example.mulga.feature.calendar.components.TransactionDaySection
import com.example.mulga.feature.calendar.components.TransactionList
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp) // 자식 요소 사이에 12.dp 간격 적용
    ) {
        CalendarHeaderView(viewModel = viewModel)

        if (uiState.selectedToggleIndex == 0) {
            uiState.dailySummariesData?.let { summaries ->
                CustomCalendarView(
                    year = uiState.currentYear,
                    month = uiState.currentMonth,
                    dailySummariesData = summaries,
                    selectedDate = uiState.selectedDate,
                    onDateClick = { clickedDate -> viewModel.onDateSelected(clickedDate) }
                )
            }
            uiState.selectedDate?.let { selectedDate ->
                val dailyTransaction = uiState.dailyTransactionsData?.find { it.date == selectedDate }
                if (dailyTransaction != null) {
                    TransactionDaySection(
                        dailyTransactionData = dailyTransaction,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        standalone = true
                    )
                }
            }
        } else {
            uiState.dailyTransactionsData?.let {
                TransactionList(
                    dailyTransactionDataList = it,
                    selectedDate = uiState.selectedDate
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    val fakeViewModel = CalendarViewModel()
    CalendarScreen(viewModel = fakeViewModel)
}