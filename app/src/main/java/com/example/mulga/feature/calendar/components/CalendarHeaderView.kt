package com.example.mulga.feature.calendar.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mulga.R
import com.example.mulga.feature.calendar.CalendarViewModel
import com.example.mulga.feature.component.toggle.ToggleSwitch
import com.example.mulga.ui.theme.MulGaTheme

@Composable
fun CalendarHeaderView(viewModel: CalendarViewModel) {
    // ViewModel의 UI 상태를 구독합니다.
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // 상단 Row: MonthSelector와 우측 plus 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // MonthSelector는 좌측에 배치
            Box(modifier = Modifier.weight(1f)) {
                MonthSelector(
                    currentYear = uiState.currentYear,
                    currentMonth = uiState.currentMonth,
                    onPrevClick = { viewModel.onPrevMonthClick() },
                    onNextClick = { viewModel.onNextMonthClick() }
                )
            }
            // 우측 plus 버튼
            Image(
                painter = painterResource(id = R.drawable.ic_util_plus),
                contentDescription = "추가",
                modifier = Modifier
                    .size(32.dp)
                    .padding(horizontal = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { viewModel.onPlusClick() },
                colorFilter = ColorFilter.tint(MulGaTheme.colors.grey1)
            )
        }
        // 하단 Row: 총 지출 금액 텍스트와 토글 스위치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MonthlyTotalSpendingText(amount = uiState.totalSpending)
            ToggleSwitch(
                selectedIndex = uiState.selectedToggleIndex,
                onOptionSelected = { newIndex -> viewModel.onToggleOptionSelected(newIndex) },
                firstLabel = "달력",
                secondLabel = "목록"
            )
        }
    }
}