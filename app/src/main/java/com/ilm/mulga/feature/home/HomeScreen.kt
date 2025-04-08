package com.ilm.mulga.feature.home

import WaveBackground
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.feature.home.components.ExpenseSummaryView
import com.ilm.mulga.feature.home.components.RecentExpenseView
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing.value = true
                viewModel.loadAndConvertHomeData()
                isRefreshing.value = false
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadAndConvertHomeData()
    }

    // 최상위 Box에 pullRefresh 적용
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        // 스크롤 가능한 컨테이너로 변경 (verticalScroll 추가)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            ExpenseSummaryView(
                monthTotal = uiState.monthTotal,
                remainingBudget = uiState.remainingBudget,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            )
            WaveBackground(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                baselineFraction = uiState.baselineFraction
            )
            Spacer(modifier = Modifier.weight(0.05f))
            RecentExpenseView(
                item = uiState.lastTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f)
            )
        }

        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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