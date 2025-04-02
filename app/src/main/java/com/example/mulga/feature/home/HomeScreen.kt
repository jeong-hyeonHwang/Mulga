package com.ilm.mulga.feature.home

import WaveBackground
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.feature.home.components.ExpenseSummaryView
import com.ilm.mulga.feature.home.components.RecentExpenseView
import com.ilm.mulga.presentation.model.TransactionItemData
import com.ilm.mulga.presentation.model.type.Category

@Composable
fun HomeScreen() {
    val item = TransactionItemData(
        category = Category.FOOD,
        title = "냠",
        subtitle = "어딘가 어떤 카드로",
        price = "50000",
        time = "some"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(90.dp))
        ExpenseSummaryView(modifier = Modifier.fillMaxWidth())

        // 중간: 남은 공간을 WaveBackground가 채움
        // weight(1f)를 사용하면, 나머지 공간을 모두 차지하게 됩니다.
        WaveBackground(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            waveHeight = 0.dp // 내부에서 Canvas의 높이는 부모의 제약을 사용함
        )

        RecentExpenseView(
            item = item,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp)
        )
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}