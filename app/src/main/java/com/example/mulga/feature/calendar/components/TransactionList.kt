package com.example.mulga.feature.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.presentation.model.DailyTransactionData
import com.example.mulga.presentation.model.TransactionItemData
import com.example.mulga.presentation.model.type.Category
import java.time.LocalDate

@Composable
fun TransactionList(
    dailyTransactionDataList: List<DailyTransactionData>,
    selectedDate: LocalDate?
) {
    val filteredList = dailyTransactionDataList.filter { it.transactions.isNotEmpty() }
    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate) {
        val newIndex = filteredList.indexOfFirst { it.date == selectedDate }
        listState.scrollToItem(newIndex)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(filteredList) { dailyTransactionData ->
            TransactionDaySection(dailyTransactionData = dailyTransactionData)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionListPreview() {
    // 1) 날짜별로 TransactionItemModel 리스트를 만든다
    val day1 = DailyTransactionData(
        date = LocalDate.of(2025, 3, 27),
        transactions = listOf(
            TransactionItemData(
                category = Category.CAFE,
                title = "아이스 아메리카노",
                subtitle = "LGU+ 카드의 정식 | 메가커피",
                price = "-1500",
                time = "08:23"
            ),

            TransactionItemData(
                category = null,
                title = "무엇인지 잘 몰라요",
                subtitle = "LGU+ 카드의 정식 | 메가커피",
                price = "-2455500",
                time = "08:23"
            )
        )
    )
    val day2 = DailyTransactionData(
        date = LocalDate.of(2025, 3, 26),
        transactions = listOf())
    val day3 = DailyTransactionData(
        LocalDate.of(2025, 3, 25),
        transactions = listOf(
            TransactionItemData(
                category = Category.BEAUTY,
                title = "화장품",
                subtitle = "쿠팡 | 로드샵",
                price = "-12400",
                time = "09:10"
            ),
            TransactionItemData(
                category = Category.FOOD,
                title = "점심 식사",
                subtitle = "배달의 민족 | 국밥",
                price = "-8000",
                time = "12:35"
            )
        )
    )

    // 2) 여러 날짜 묶어서 리스트 생성
    val dayModels = listOf(day1, day2, day3)

    // 3) TransactionListScreen에 전달
    TransactionList(
        dailyTransactionDataList = dayModels,
        selectedDate = LocalDate.now()
    )
}
