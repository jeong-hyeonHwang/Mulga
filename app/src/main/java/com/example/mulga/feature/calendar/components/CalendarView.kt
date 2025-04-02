package com.ilm.mulga.feature.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilm.mulga.presentation.model.DailyTransactionSummaryData
import com.ilm.mulga.ui.theme.MulGaTheme
import java.time.LocalDate
import java.time.YearMonth


/**
 * 실제 달력 컴포저블
 */
@Composable
fun CustomCalendarView(
    year: Int,
    month: Int,
    dailySummariesData: List<DailyTransactionSummaryData>,
    selectedDate: LocalDate?,  // 외부 상태 전달
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    // 이 달의 정보
    val yearMonth = YearMonth.of(year, month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val firstDayOfWeekIndex = (firstDayOfMonth.dayOfWeek.value - 1) % 7

    // 요일 헤더 텍스트
    val daysOfWeek = listOf("월", "화", "수", "목", "금", "토", "일")

    // 빈 칸(null)와 실제 데이터(모든 날짜)를 포함하는 리스트 구성
    val dayDataList = buildList<DailyTransactionSummaryData?> {
        repeat(firstDayOfWeekIndex) { add(null) }
        for (day in 1..daysInMonth) {
            val summary = dailySummariesData.find { it.date.dayOfMonth == day }
            // 데이터가 없으면 기본값 생성 (isValid false)
            add(summary ?: DailyTransactionSummaryData(isValid = false, income = 0, expense = 0, date = LocalDate.of(year, month, day)))
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MulGaTheme.colors.grey5)
    ) {
        Spacer(Modifier.height(16.dp))
        // 요일 헤더
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { dayOfWeek ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayOfWeek,
                        style = MulGaTheme.typography.caption,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        // 날짜 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dayDataList) { dayData ->
                if (dayData != null) {
                    DayCell(
                        dayData = dayData,
                        isSelected = dayData.date == selectedDate,  // 외부 selectedDate와 비교
                        onClick = onDateClick
                    )
                } else {
                    // 빈 칸 처리
                    Box(modifier = Modifier.size(40.dp).padding(4.dp))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

/**
 * 달력의 각 날짜 칸
 */
@Composable
fun DayCell(
    dayData: DailyTransactionSummaryData,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
) {
    // 지출/수입을 보기 좋게 포맷 (예: 123,000), 지출은 음수, 수입은 양수
    val expenseText = if (dayData.expense != 0) String.format("-%,d", dayData.expense) else ""
    val incomeText = if (dayData.income != 0) String.format("+%,d", dayData.income) else ""

    // 지출 수입 색 지정
    val expenseColor = MulGaTheme.colors.primary
    val incomeColor = MulGaTheme.colors.grey2

    // 날짜 텍스트의 기본 색상 (유효하면 선택 여부에 따라, 아니면 회색)
    val dateTextColor = if (!dayData.isValid) MulGaTheme.colors.grey2
    else if (isSelected) MulGaTheme.colors.white1
    else MulGaTheme.colors.black1

    // 클릭 가능한 Modifier: isValid가 false면 clickable 효과를 주지 않음
    val clickableModifier = if (dayData.isValid) {
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick(dayData.date) }
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .then(clickableModifier),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 날짜 숫자를 원 안에 표시 (선택된 경우에만 원 배경 적용)
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MulGaTheme.colors.primary else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                // 내부 내용
                Text(
                    text = dayData.date.dayOfMonth.toString(),
                    style = MulGaTheme.typography.caption,
                    color = dateTextColor
                )
            }
            Column (
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (dayData.expense == 0 && dayData.income != 0) {
                    // 지출이 없는 경우, 수입 텍스트만 상단에 정렬
                    Text(
                        text = incomeText,
                        style = MulGaTheme.typography.label,
                        color = incomeColor
                    )
                } else {
                    // 지출 표시
                    Text(
                        text = expenseText,
                        style = MulGaTheme.typography.label,
                        color = expenseColor
                    )
                    // 수입 표시
                    Text(
                        text = incomeText,
                        style = MulGaTheme.typography.label,
                        color = incomeColor
                    )
                }
            }
        }
    }
}