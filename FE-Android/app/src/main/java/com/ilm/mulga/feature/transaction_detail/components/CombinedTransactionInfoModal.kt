package com.ilm.mulga.feature.transaction_detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilm.mulga.feature.calendar.components.TransactionDaySection
import com.ilm.mulga.presentation.model.DailyTransactionData
import com.ilm.mulga.ui.theme.MulGaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinedTransactionInfoModal(
    combinedTransactions: List<DailyTransactionData>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val gridState = rememberLazyGridState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MulGaTheme.colors.white1,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                state = gridState,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                // Enable user scrolling
                userScrollEnabled = true,
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(combinedTransactions) { dailyData ->
                    TransactionDaySection(
                        dailyTransactionData = dailyData,
                        onTransactionClick = { },
                        onTransactionLongClick = { },
                        isDeleteMode = false,
                        selectedItemIds = emptySet(),
                        standalone = false,
                        // Don't use fillMaxSize() here to avoid height conflicts
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}