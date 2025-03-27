package com.example.mulga.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mulga.ui.theme.MulGaTheme
import java.time.LocalDate
import java.time.YearMonth


/**
 * 달력에 표시할 데이터 예시용
 * - day: 날짜 (1~31)
 * - expense: 지출(음수)
 * - income: 수입(양수)
 */
data class DayData(
    val date: LocalDate,
    val expense: Int = 0,
    val income: Int = 0
)

/**
 * 실제 달력 컴포저블
 */
@Composable
fun CustomCalendarView(
    year: Int,
    month: Int
) {
    // 선택된 날짜 상태 (예: 일자 Int 혹은 LocalDate 등으로 관리)
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    // 이 달의 정보
    val yearMonth = YearMonth.of(year, month)
    val daysInMonth = yearMonth.lengthOfMonth() // 해당 월의 일 수
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    // 첫날의 요일 (월=1, 화=2, ..., 일=7) -> (월=0, 화=1, ..., 일=6)
    val firstDayOfWeekIndex = (firstDayOfMonth.dayOfWeek.value - 1) % 7

    // 요일 헤더 텍스트
    val daysOfWeek = listOf("월", "화", "수", "목", "금", "토", "일")

    // 실제로 달력에 표시할 DayData 리스트 구성
    val dayDataList = buildList {
        // 첫날 요일 전까지의 빈 칸을 처리하기 위해 null 데이터를 넣는다
        repeat(firstDayOfWeekIndex) {
            add(null)
        }
        // 1일부터 말일까지 DayData 생성
        for (day in 1..daysInMonth) {
            val date = LocalDate.of(year, month, day)
            // TODO: DB나 API에서 날짜별 지출/수입 데이터를 받아와서 매핑
            // 일단 랜덤값 사용
            val expense = (1000..100000).random()
            val income = (1000..1000000).random()
            add(DayData(date, expense, income))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // 월/연도 확인용
//        Text(
//            text = "${year}년 ${month}월",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(bottom = 12.dp)
//        )

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
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(dayDataList) { dayData ->
                if (dayData != null) {
                    DayCell(
                        dayData = dayData,
                        isSelected = dayData.date == selectedDate,
                        onClick = { clickedDate ->
                            selectedDate = clickedDate
                        }
                    )
                }
            }
        }
    }
}

/**
 * 달력의 각 날짜 칸
 */
@Composable
fun DayCell(
    dayData: DayData,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
) {
    // 지출/수입을 보기 좋게 포맷 (예: 123,000), 지출은 음수, 수입은 양수
    val expenseText = String.format("-%,d", dayData.expense)
    val incomeText = String.format("+%,d", dayData.income)

    // 지출 수입 색 지정
    val expenseColor = MulGaTheme.colors.primary
    val incomeColor = MulGaTheme.colors.grey2

    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable { onClick(dayData.date) },
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
                    .then(
                        if (isSelected)
                            Modifier
                                .clip(CircleShape)
                                .background(MulGaTheme.colors.primary)
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayData.date.dayOfMonth.toString(),
                    style = MulGaTheme.typography.caption,
                    color = if (isSelected) MulGaTheme.colors.white1 else MulGaTheme.colors.black1
                )
            }
            Column (
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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


@Preview(showBackground = true)
@Composable
fun CustomCalendarPreview() {
    CustomCalendarView(year = 2025, month = 3)
}