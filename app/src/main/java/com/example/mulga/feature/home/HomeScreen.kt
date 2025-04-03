package com.example.mulga.feature.home

import WaveBackground
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.R
import com.example.mulga.feature.home.components.ExpenseSummaryView
import com.example.mulga.feature.home.components.RecentExpenseView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // Box를 사용하여 전체 레이아웃을 감싸면, 자식들을 Z-축으로 쌓을 수 있음
    Box(modifier = Modifier.fillMaxSize()) {
        // 기본 콘텐츠: Column에 배치
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(90.dp))
            ExpenseSummaryView(
                monthTotal = uiState.monthTotal,
                remainingBudget = uiState.remainingBudget,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(40.dp))
            // WaveBackground는 Column의 weight(1f)를 사용해 나머지 영역을 채웁니다.

            WaveBackground(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                baselineFraction = uiState.baselineFraction
            )
            Spacer(modifier = Modifier.height(16.dp))

            RecentExpenseView(
                item = uiState.lastTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp)
            )
        }

        // Box의 최상단 레이어에, baselineFraction이 1.0f일 때 물고기 이미지를 중앙에 오버레이합니다.
        if (uiState.baselineFraction == 1.0f) {
            Image(
                painter = painterResource(id = R.drawable.ic_util_fish),
                contentDescription = "Fish",
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* 물고기 이미지 클릭 시 액션 */}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Preview에서는 직접 HomeViewModel 인스턴스를 생성합니다.
    // 실제 앱에서는 koinViewModel()을 통해 DI로 주입받습니다.
    val fakeViewModel = HomeViewModel()
    HomeScreen(viewModel = fakeViewModel)
}