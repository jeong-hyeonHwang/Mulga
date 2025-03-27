package com.example.mulga.feature.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.feature.calendar.components.CalendarHeaderView
import com.example.mulga.feature.calendar.components.TransactionList
import com.example.mulga.ui.theme.LocalMulGaTypography
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        CalendarHeaderView(viewModel = viewModel)
        Spacer(modifier = Modifier.height(12.dp))
        if (uiState.selectedToggleIndex == 0) {
            // TODO: 캘린더형
            Text("캘린더형 요소")
        } else {
            // TODO: 리스트형
            TransactionList(dayModels = uiState.dayModels)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    // Preview에서는 직접 CalendarViewModel 인스턴스를 생성합니다.
    // (실제 앱에서는 koinViewModel()을 사용하여 DI로 주입받습니다.)
    val fakeViewModel = CalendarViewModel()

    CalendarScreen(viewModel = fakeViewModel)
}