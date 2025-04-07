package com.ilm.mulga.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ilm.mulga.R
import com.ilm.mulga.feature.calendar.components.CalendarHeaderView
import com.ilm.mulga.feature.calendar.components.CustomCalendarView
import com.ilm.mulga.feature.calendar.components.TransactionDaySection
import com.ilm.mulga.feature.calendar.components.TransactionList
import com.ilm.mulga.presentation.mapper.toPresentation
import com.ilm.mulga.ui.theme.MulGaTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = koinViewModel(),
                   navController: NavController) {
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

            if (uiState.selectedDate == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(id = R.string.calendar_no_selected_date),
                        style = MulGaTheme.typography.bodyLarge,
                        color = MulGaTheme.colors.grey1
                    )
                }
            }

            uiState.selectedDate?.let { selectedDate ->
                val dailyTransaction = uiState.dailyTransactionsData?.find { it.date == selectedDate }
                if (dailyTransaction != null) {
                    TransactionDaySection(
                        dailyTransactionData = dailyTransaction,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        standalone = true,
                        onTransactionClick = { transactionId ->
                            // 월간 데이터의 transactions에서 해당 거래를 찾는다.
                            val transactionEntity = uiState.monthlyTransactionEntity
                                ?.transactions
                                ?.values
                                ?.flatten()
                                ?.find { it.id == transactionId }
                            transactionEntity?.let { entity ->
                                // 확장 함수 toPresentation()을 사용해 Presentation 데이터로 변환
                                val detailData = entity.toPresentation()
                                // JSON으로 인코딩
                                val jsonData = Json.encodeToString(detailData)
                                // URL 안전하게 인코딩
                                val encodedData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8.toString())
                                navController.navigate("transaction_detail?data=$encodedData")
                            }
                        },
                    )
                }
            }
        } else {
            uiState.dailyTransactionsData?.let { dailyData ->
                val allTransactions = dailyData.flatMap { it.transactions }
                if (allTransactions.isEmpty()) {
                    // 모든 날짜에 거래 내역이 없는 경우 보여줄 뷰
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.calendar_no_transaction_in_month),
                            style = MulGaTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MulGaTheme.colors.grey1
                        )
                    }
                } else {
                    TransactionList(
                        dailyTransactionDataList = dailyData,
                        selectedDate = uiState.selectedDate,
                        onTransactionClick = { transactionId ->
                            // 위와 동일한 로직으로 상세 데이터 찾기
                            val transactionEntity = uiState.monthlyTransactionEntity
                                ?.transactions
                                ?.values
                                ?.flatten()
                                ?.find { it.id == transactionId }
                            transactionEntity?.let { entity ->
                                val detailData = entity.toPresentation()
                                val jsonData = Json.encodeToString(detailData)
                                val encodedData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8.toString())
                                navController.navigate("transaction_detail?data=$encodedData")
                            }
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    val fakeViewModel = CalendarViewModel()
    CalendarScreen(
        viewModel = fakeViewModel,
        navController = rememberNavController()
    )
}