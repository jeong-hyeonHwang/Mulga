package com.ilm.mulga.feature.calendar

import DeleteConfirmDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ilm.mulga.R
import com.ilm.mulga.feature.calendar.components.CalendarHeaderView
import com.ilm.mulga.feature.calendar.components.CustomCalendarView
import com.ilm.mulga.feature.calendar.components.TransactionBatchPanel
import com.ilm.mulga.feature.calendar.components.TransactionDaySection
import com.ilm.mulga.feature.calendar.components.TransactionList
import com.ilm.mulga.ui.theme.MulGaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel = koinViewModel(),
    transactionItemViewModel: TransactionItemViewModel = koinViewModel(),
                   onNavigateToTransactionAdd: () -> Unit = {},
                   navController: NavController) {
    val uiState by calendarViewModel.uiState.collectAsState()

    val isDeleteMode by transactionItemViewModel.isDeleteMode.collectAsState()
    val selectedItemIds by transactionItemViewModel.selectedItemIds.collectAsState()
    LaunchedEffect(key1 = true) {
        calendarViewModel.navigationEvent.collect { event ->
            when (event) {
                "transaction_add" -> onNavigateToTransactionAdd()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp) // 자식 요소 사이에 12.dp 간격 적용
    ) {
        CalendarHeaderView(viewModel = calendarViewModel)

        // 삭제 확인 다이얼로그 표시 여부를 관리할 상태 변수
        val showDeleteConfirmDialog = remember { mutableStateOf(false) }

        DisposableEffect(Unit) {
            onDispose {
                transactionItemViewModel.clearDeleteMode()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(bottom = if (isDeleteMode) 72.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                if (uiState.selectedToggleIndex == 0) {
                    uiState.dailySummariesData?.let { summaries ->
                        CustomCalendarView(
                            year = uiState.currentYear,
                            month = uiState.currentMonth,
                            dailySummariesData = summaries,
                            selectedDate = uiState.selectedDate,
                            onDateClick = { clickedDate ->
                                calendarViewModel.onDateSelected(clickedDate)
                            }
                        )
                    }

                    if (uiState.selectedDate == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stringResource(id = R.string.calendar_no_selected_date),
                                style = MulGaTheme.typography.bodyLarge,
                                color = MulGaTheme.colors.grey1
                            )
                        }
                    }

                    uiState.selectedDate?.let { selectedDate ->
                        val dailyTransaction =
                            uiState.dailyTransactionsData?.find { it.date == selectedDate }
                        if (dailyTransaction != null) {
                            TransactionDaySection(
                                dailyTransactionData = dailyTransaction,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                standalone = true,
                                onTransactionClick = { transactionId ->
                                    transactionItemViewModel.onItemClick(transactionId) {
                                        calendarViewModel.onTransactionClick(
                                            transactionId,
                                            navController
                                        )
                                    }
                                },
                                onTransactionLongClick = { transactionId ->
                                    transactionItemViewModel.onItemLongPress(transactionId)
                                },
                                isDeleteMode = isDeleteMode,
                                selectedItemIds = selectedItemIds
                            )
                        }
                    }
                } else {
                    uiState.dailyTransactionsData?.let { dailyData ->
                        val allTransactions = dailyData.flatMap { it.transactions }
                        if (allTransactions.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.calendar_no_transaction_in_month),
                                    style = MulGaTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MulGaTheme.colors.grey1
                                )
                            }
                        } else {
                            TransactionList(
                                dailyTransactionDataList = dailyData,
                                selectedDate = uiState.selectedDate,
                                onTransactionClick = { transactionId ->
                                    transactionItemViewModel.onItemClick(transactionId) {
                                        calendarViewModel.onTransactionClick(
                                            transactionId,
                                            navController
                                        )
                                    }
                                },
                                onTransactionLongClick = { transactionId ->
                                    transactionItemViewModel.onItemLongPress(transactionId)
                                },
                                isDeleteMode = isDeleteMode,
                                selectedItemIds = selectedItemIds
                            )
                        }
                    }
                }
            }

            if (isDeleteMode) {
                TransactionBatchPanel(
                    onMergeClick = {
                        // 합치기 처리 로직 구현
                    },
                    onDeleteClick = {
                        // 삭제 버튼 클릭 시, 다이얼로그 표시
                        showDeleteConfirmDialog.value = true
                    },
                    onCancelClick = {
                        transactionItemViewModel.clearDeleteMode()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }

        // 삭제 확인 다이얼로그를 조건부로 표시
        if (showDeleteConfirmDialog.value) {
            DeleteConfirmDialog(
                dataCount = selectedItemIds.size,
                onCancel = { showDeleteConfirmDialog.value = false },
                onConfirm = {
                    calendarViewModel.deleteTransactionItems(selectedItemIds)
                    transactionItemViewModel.clearDeleteMode()
                    showDeleteConfirmDialog.value = false
                },
            )
        }
    }
}
