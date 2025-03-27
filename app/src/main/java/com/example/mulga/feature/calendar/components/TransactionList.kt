package com.example.mulga.feature.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.model.TransactionDayModel
import com.example.mulga.model.TransactionItemModel
import com.example.mulga.model.enums.Category

@Composable
fun TransactionList(
    dayModels: List<TransactionDayModel>
) {
    // 만약 스크롤이 필요한 양이라면 LazyColumn을 사용
    androidx.compose.foundation.lazy.LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(dayModels.size) { index ->
            val dayModel = dayModels[index]
            TransactionDaySection(dayModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListPreview() {
    // 1) 날짜별로 TransactionItemModel 리스트를 만든다
    val day1 = TransactionDayModel(
        year = 2025,
        month = 3,
        day = 27,
        transactions = listOf(
            TransactionItemModel(
                category = Category.CAFE,
                title = "아이스 아메리카노",
                subtitle = "LGU+ 카드의 정식 | 메가커피",
                price = "-1500",
                time = "08:23"
            ),
            TransactionItemModel(
                category = null,
                title = "무엇인지 잘 몰라요",
                subtitle = "LGU+ 카드의 정식 | 메가커피",
                price = "-2455500",
                time = "08:23"
            )
        )
    )

    val day2 = TransactionDayModel(
        year = 2025,
        month = 3,
        day = 26,
        transactions = listOf(
            TransactionItemModel(
                category = Category.BEAUTY,
                title = "화장품",
                subtitle = "쿠팡 | 로드샵",
                price = "-12400",
                time = "09:10"
            ),
            TransactionItemModel(
                category = Category.FOOD,
                title = "점심 식사",
                subtitle = "배달의 민족 | 국밥",
                price = "-8000",
                time = "12:35"
            )
        )
    )

    // 2) 여러 날짜 묶어서 리스트 생성
    val dayModels = listOf(day1, day2)

    // 3) TransactionListScreen에 전달
    TransactionList(dayModels = dayModels)
}
