package com.ilm.mulga.feature.calendar.components

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
import com.ilm.mulga.presentation.model.DailyTransactionData
import com.ilm.mulga.presentation.model.TransactionItemData
import com.ilm.mulga.presentation.model.type.Category
import java.time.LocalDate

@Composable
fun TransactionList(
    dailyTransactionDataList: List<DailyTransactionData>,
    selectedDate: LocalDate?,
    onTransactionClick: (transactionId: String) -> Unit
) {
    val filteredList = dailyTransactionDataList.filter { it.transactions.isNotEmpty() }
    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate) {
        val newIndex = filteredList.indexOfFirst { it.date == selectedDate }
        if (newIndex >= 0) {
            listState.scrollToItem(newIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(filteredList) { dailyTransactionData ->
            TransactionDaySection(
                dailyTransactionData = dailyTransactionData,
                onTransactionClick = onTransactionClick)
        }
    }
}