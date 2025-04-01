package com.example.mulga.feature.calendar.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mulga.R
import com.example.mulga.presentation.model.DailyTransactionData

@Composable
fun TransactionDaySection(
    dailyTransactionData: DailyTransactionData,
    modifier: Modifier = Modifier,
    standalone: Boolean = false  // true면 standalone 모드, false면 List 모드
) {
    if (standalone) {
        // standalone 모드: 헤더는 고정, 거래 내역 부분은 스크롤 가능하도록 분리
        Column(modifier = modifier.padding(horizontal = 12.dp)) {
            // 날짜 헤더는 고정
            TransactionDayHeader(
                dateLabel = stringResource(
                    id = R.string.calendar_month_day,
                    dailyTransactionData.date.monthValue,
                    dailyTransactionData.date.dayOfMonth
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 거래 내역 목록을 스크롤 가능 영역으로 감싼다.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    if (dailyTransactionData.transactions.isEmpty()) {
                        Text(
                            text = "선택한 날짜에 거래 내역이 없습니다.",
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                    } else {
                        dailyTransactionData.transactions.forEach { item ->
                            TransactionItem(item = item)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    } else {
        // List 모드: 전체 TransactionDaySection이 펼쳐진 상태 (스크롤은 외부 LazyColumn이 담당)
        Column(modifier = modifier.padding(horizontal = 12.dp)) {
            TransactionDayHeader(
                dateLabel = stringResource(
                    id = R.string.calendar_month_day,
                    dailyTransactionData.date.monthValue,
                    dailyTransactionData.date.dayOfMonth
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (dailyTransactionData.transactions.isNotEmpty()) {
                dailyTransactionData.transactions.forEach { item ->
                    TransactionItem(item = item)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
